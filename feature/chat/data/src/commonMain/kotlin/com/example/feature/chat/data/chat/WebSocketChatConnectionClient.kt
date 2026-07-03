package com.example.feature.chat.data.chat

import com.example.core.domain.auth.SessionStorage
import com.example.feature.chat.data.dto.websocket.IncomingWebSocketDto
import com.example.feature.chat.data.dto.websocket.IncomingWebSocketType
import com.example.feature.chat.data.dto.websocket.WebSocketMessageDto
import com.example.feature.chat.data.mappers.toDomain
import com.example.feature.chat.data.mappers.toEntity
import com.example.feature.chat.data.network.KtorWebSocketConnector
import com.example.feature.chat.database.ChirpChatDatabase
import com.example.feature.chat.domain.chat.ChatConnectionClient
import com.example.feature.chat.domain.chat.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketChatConnectionClient(
    private val ktorWebSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val chirpChatDatabase: ChirpChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val applicationScope: CoroutineScope
) : ChatConnectionClient {

    override val chatMessages =
        ktorWebSocketConnector
            .messages
            .mapNotNull { webSocketMessageDto -> webSocketMessageDto.parseIncomingMessage() }
            .onEach { incomingWebSocketDto -> incomingWebSocketDto.handleIncomingMessage() }
            .filterIsInstance<IncomingWebSocketDto.NewMessageDto>()
            .mapNotNull { newMessageDto ->
                chirpChatDatabase.chatMessageDao.getMessageById(
                    newMessageDto.id
                )?.toDomain()
            }
            .shareIn(
                applicationScope,
                SharingStarted.WhileSubscribed(5_000L)
            )

    override val connectionState = ktorWebSocketConnector.connectionState

    private fun WebSocketMessageDto.parseIncomingMessage(): IncomingWebSocketDto? {
        return when (type) {
            IncomingWebSocketType.NEW_MESSAGE.name -> {
                json.decodeFromString<IncomingWebSocketDto.NewMessageDto>(payload)
            }

            IncomingWebSocketType.MESSAGE_DELETED.name -> {
                json.decodeFromString<IncomingWebSocketDto.MessageDeletedDto>(payload)
            }

            IncomingWebSocketType.PROFILE_PICTURE_UPDATED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ProfilePictureUpdated>(payload)
            }

            IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED.name -> {
                json.decodeFromString<IncomingWebSocketDto.ChatParticipantsChangedDto>(payload)
            }

            else -> null
        }
    }

    private suspend fun IncomingWebSocketDto.handleIncomingMessage() {
        when (this) {
            is IncomingWebSocketDto.ChatParticipantsChangedDto -> refreshChat()
            is IncomingWebSocketDto.MessageDeletedDto -> deleteMessage()
            is IncomingWebSocketDto.NewMessageDto -> handleNewMessage()
            is IncomingWebSocketDto.ProfilePictureUpdated -> updateProfilePicture()
        }
    }

    private suspend fun IncomingWebSocketDto.ChatParticipantsChangedDto.refreshChat() =
        chatRepository.fetchChatById(chatId = chatId)

    private suspend fun IncomingWebSocketDto.MessageDeletedDto.deleteMessage() =
        chirpChatDatabase.chatMessageDao.deleteMessageById(messageId = messageId)


    private suspend fun IncomingWebSocketDto.NewMessageDto.handleNewMessage() =
        with(chirpChatDatabase) {
            val chatExists = chatDao.getChatById(chatId) != null
            if (!chatExists) {
                chatRepository.fetchChatById(chatId)
            }

            val entity = toEntity()
            chatMessageDao.upsertMessage(entity)
        }

    private suspend fun IncomingWebSocketDto.ProfilePictureUpdated.updateProfilePicture() {
        chirpChatDatabase
            .chatParticipantDao
            .updateProfilePictureUrl(
                userId = userId,
                newProfilePictureUrl = newUrl
            )

        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
        if (authInfo != null && authInfo.user.id == userId) {
            sessionStorage.set(
                info = authInfo.copy(
                    user = authInfo.user.copy(
                        profilePictureUrl = newUrl
                    )
                )
            )
        }
    }
}