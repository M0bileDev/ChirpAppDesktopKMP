package com.example.feature.chat.data.message

import com.example.core.data.database.safeDatabaseUpdate
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.feature.chat.data.dto.websocket.OutgoingWebSocketDto
import com.example.feature.chat.data.dto.websocket.WebSocketMessageDto
import com.example.feature.chat.data.mappers.toDomain
import com.example.feature.chat.data.mappers.toEntity
import com.example.feature.chat.data.mappers.toWebsocketDto
import com.example.feature.chat.data.message.ChatMessageConstants.PAGE_SIZE
import com.example.feature.chat.data.network.KtorWebSocketConnector
import com.example.feature.chat.database.ChirpChatDatabase
import com.example.feature.chat.domain.message.ChatMessageService
import com.example.feature.chat.domain.message.MessageRepository
import com.example.feature.chat.domain.model.ChatMessage
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.domain.model.MessageWithSender
import com.example.feature.chat.domain.model.OutgoingNewMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val chirpChatDatabase: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> = with(chirpChatDatabase) {
        return@with safeDatabaseUpdate {
            chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                safeDatabaseUpdate {
                    val entities = messages.map { it.toEntity() }
                    val mostRecentPage = before == null

                    chirpChatDatabase.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = entities,
                        pageSize = PAGE_SIZE,
                        shouldSync = mostRecentPage
                    )
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return chirpChatDatabase
            .chatMessageDao
            .getMessagesWithSenderByChatId(chatId)
            .map { messages ->
                messages.map { message -> message.toDomain() }
            }
    }

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> =
        with(chirpChatDatabase.chatMessageDao) {
            return safeDatabaseUpdate {
                val dto = message.toWebsocketDto()
                val localUser =
                    sessionStorage.observeAuthInfo().first()?.user ?: return Result.Failure(
                        DataError.Local.NOT_FOUND
                    )

                val entity = dto.toEntity(
                    senderId = localUser.id,
                    deliveryStatus = ChatMessageDeliveryStatus.SENDING
                )
                upsertMessage(
                    message = entity
                )

                val message = dto.toJsonPayload()
                return@with webSocketConnector
                    .sendMessage(message = message)
                    .onFailure {
                        applicationScope.launch {
                            updateDeliveryStatus(
                                messageId = entity.messageId,
                                timestamp = Clock.System.now().toEpochMilliseconds(),
                                status = ChatMessageDeliveryStatus.FAILED.name
                            )
                        }.join()
                    }
            }
        }

    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.name,
            payload = json.encodeToString(this)
        )

        return json.encodeToString(webSocketMessage)
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> =
        with(chirpChatDatabase.chatMessageDao) {
            return safeDatabaseUpdate {
                val message = getMessageById(messageId) ?: return@with Result.Failure(
                    DataError.Local.NOT_FOUND
                )
                updateDeliveryStatus(
                    messageId = messageId,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                    status = ChatMessageDeliveryStatus.SENDING.name
                )

                val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                    chatId = message.chatId,
                    messageId = messageId,
                    content = message.content
                )

                return@with webSocketConnector
                    .sendMessage(outgoingNewMessage.toJsonPayload())
                    .onFailure {
                        applicationScope.launch {
                            updateDeliveryStatus(
                                messageId = messageId,
                                timestamp = Clock.System.now().toEpochMilliseconds(),
                                status = ChatMessageDeliveryStatus.FAILED.name
                            )
                        }.join()
                    }
            }
        }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return chatMessageService
            .deleteMessage(messageId = messageId)
            .onSuccess {
                applicationScope.launch {
                    chirpChatDatabase
                        .chatMessageDao
                        .deleteMessageById(messageId = messageId)
                }.join()
            }
    }
}