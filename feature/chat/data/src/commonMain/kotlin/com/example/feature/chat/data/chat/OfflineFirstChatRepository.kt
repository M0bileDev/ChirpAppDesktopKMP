package com.example.feature.chat.data.chat

import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyResult
import com.example.core.domain.util.onSuccess
import com.example.feature.chat.data.mappers.toDomain
import com.example.feature.chat.data.mappers.toEntity
import com.example.feature.chat.data.mappers.toLastMessageView
import com.example.feature.chat.database.ChirpChatDatabase
import com.example.feature.chat.database.entities.ChatInfoEntity
import com.example.feature.chat.database.entities.ChatParticipantEntity
import com.example.feature.chat.database.entities.ChatWithParticipants
import com.example.feature.chat.domain.chat.ChatRepository
import com.example.feature.chat.domain.chat.ChatService
import com.example.feature.chat.domain.model.Chat
import com.example.feature.chat.domain.model.ChatInfo
import com.example.feature.chat.domain.model.ChatParticipant
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class OfflineFirstChatRepository(
    private val chatService: ChatService,
    private val chirpChatDatabase: ChirpChatDatabase
) : ChatRepository {

    override fun getChats(): Flow<List<Chat>> {
        return chirpChatDatabase
            .chatDao
            .getChatsWithParticipants()
            .map { chatsWithParticipants ->
                supervisorScope {
                    chatsWithParticipants
                        .map { chatWithParticipants ->
                            async {
                                ChatWithParticipants(
                                    chat = chatWithParticipants.chat,
                                    participants = chatWithParticipants.participants.onlyActive(
                                        chatWithParticipants.chat.chatId
                                    ),
                                    lastMessage = chatWithParticipants.lastMessage
                                )
                            }
                        }.awaitAll()
                        .map { it.toDomain() }
                }
            }

    }

    override suspend fun fetchChats(): Result<List<Chat>, DataError.Remote> {
        return chatService
            .getChats()
            .onSuccess { chats ->
                saveChats(chats)
            }
    }

    private suspend fun saveChats(chats: List<Chat>) = with(chirpChatDatabase) {
        val chatsWithParticipants = chats.map { chat ->
            ChatWithParticipants(
                chat = chat.toEntity(),
                participants = chat.participants.map { participant -> participant.toEntity() },
                lastMessage = chat.lastMessage?.toLastMessageView()
            )
        }
        chatDao
            .upsertChatsWithParticipantsAndCrossRefs(
                chatsWithParticipants = chatsWithParticipants,
                participantDao = chatParticipantDao,
                crossRefDao = chatParticipantsCrossRefDao,
                messageDao = chatMessageDao
            )
    }

    override suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote> =
        with(chirpChatDatabase) {
            return@with chatService
                .getChatById(chatId)
                .onSuccess { chat ->
                    chatDao.upsertChatWithParticipantsAndCrossRefs(
                        chat = chat.toEntity(),
                        participants = chat.participants.map { it.toEntity() },
                        participantDao = chatParticipantDao,
                        crossRefDao = chatParticipantsCrossRefDao
                    )
                }.asEmptyResult()
        }

    override fun getChatInfoById(chatId: String): Flow<ChatInfo> = with(chirpChatDatabase) {
        return@with chatDao
            .getChatInfoById(chatId)
            .filterNotNull()
            .map { chatInfoEntity ->
                ChatInfoEntity(
                    chat = chatInfoEntity.chat,
                    participants = chatInfoEntity.participants.onlyActive(chatInfoEntity.chat.chatId),
                    messagesWithSenders = chatInfoEntity.messagesWithSenders
                )
            }
            .map { it.toDomain() }
    }

    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> =
        with(chirpChatDatabase) {
            return chatService
                .createChat(otherUserIds)
                .onSuccess { chat ->
                    chatDao.upsertChatWithParticipantsAndCrossRefs(
                        chat = chat.toEntity(),
                        participants = chat.participants.map { it.toEntity() },
                        participantDao = chatParticipantDao,
                        crossRefDao = chatParticipantsCrossRefDao
                    )
                }
        }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return chatService
            .leaveChat(chatId)
            .onSuccess {
                chirpChatDatabase.chatDao.deleteChatById(chatId)
            }
    }

    private suspend fun List<ChatParticipantEntity>.onlyActive(chatId: String): List<ChatParticipantEntity> {
        val activeParticipantIds = chirpChatDatabase
            .chatDao
            .getActiveParticipantsByChatId(chatId)
            .first()
            .map { it.userId }

        return this.filter { it.userId in activeParticipantIds }
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> = with(chirpChatDatabase) {
        return@with chatService
            .addParticipantsToChat(
                chatId = chatId,
                userIds = userIds
            )
            .onSuccess { chat ->
                chatDao.upsertChatWithParticipantsAndCrossRefs(
                    chat = chat.toEntity(),
                    participants = chat.participants.map { it.toEntity() },
                    participantDao = chatParticipantDao,
                    crossRefDao = chatParticipantsCrossRefDao
                )
            }
    }

    override fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipant>> {
        return chirpChatDatabase
            .chatDao
            .getActiveParticipantsByChatId(
                chatId = chatId
            )
            .map { participants ->
                participants
                    .map { participant ->
                        participant.toDomain()
                    }
            }
    }

    override suspend fun deleteAllChats() =
        chirpChatDatabase.chatDao.deleteAllChats()

}