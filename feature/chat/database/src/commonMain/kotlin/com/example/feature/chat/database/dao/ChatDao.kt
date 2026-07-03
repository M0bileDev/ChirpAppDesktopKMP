package com.example.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.feature.chat.database.entities.ChatEntity
import com.example.feature.chat.database.entities.ChatInfoEntity
import com.example.feature.chat.database.entities.ChatMessageEntity
import com.example.feature.chat.database.entities.ChatParticipantCrossRef
import com.example.feature.chat.database.entities.ChatParticipantEntity
import com.example.feature.chat.database.entities.ChatWithParticipants
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Query("DELETE FROM chatentity WHERE chatId = :chatId")
    suspend fun deleteChatById(chatId: String)

    @Transaction
    @Query("""
        SELECT c.*
        FROM chatentity AS c
        LEFT JOIN (
            SELECT chatId, MAX(timestamp) as latest_message_time
            FROM chatmessageentity
            GROUP BY chatId
        ) AS last_message 
        ON c.chatId = last_message.chatId
        ORDER BY COALESCE(last_message.latest_message_time, c.lastActivityAt) DESC
    """)
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipants>>

    @Transaction
    @Query("SELECT * FROM chatentity WHERE chatId = :chatId")
    suspend fun getChatById(chatId: String): ChatWithParticipants?

    @Query("DELETE FROM chatentity")
    suspend fun deleteAllChats()

    @Query("SELECT chatId FROM chatentity")
    suspend fun getAllChatIds(): List<String>

    @Transaction
    suspend fun deleteChatsByIds(chatIds: List<String>) {
        chatIds.forEach { chatId ->
            deleteChatById(chatId)
        }
    }

    @Query("SELECT COUNT(*) FROM chatentity")
    fun getChatCount(): Flow<Int>

    @Query(
        """
        SELECT p.*
        FROM chatparticipantentity p
        JOIN chatparticipantcrossref c
        ON p.userId == c.userId
        WHERE c.chatId = :chatId AND c.isActive
        ORDER BY p.username
    """
    )
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    @Transaction
    @Query(
        """
        SELECT c.* 
        FROM chatentity AS c
        WHERE c.chatId= :chatId
    """
    )
    fun getChatInfoById(chatId: String): Flow<ChatInfoEntity?>

    @Transaction
    suspend fun upsertChatWithParticipantsAndCrossRefs(
        chat: ChatEntity,
        participants: List<ChatParticipantEntity>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao
    ) {
        upsertChat(chat)
        participantDao.upsertParticipants(participants)

        val crossRefs = participants.map {
            ChatParticipantCrossRef(
                chatId = chat.chatId,
                userId = it.userId,
                isActive = true
            )
        }
        with(crossRefDao) {
            upsertCrossRefs(crossRefs)
            syncChatParticipants(chat.chatId, participants)
        }

    }

    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chatsWithParticipants: List<ChatWithParticipants>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao,
        messageDao: ChatMessageDao
    ) {
        upsertChats(chatsWithParticipants.map { it.chat })

        val localChatIds = getAllChatIds()
        val serverChatIds = chatsWithParticipants.map { it.chat.chatId }
        val staleChatIds = localChatIds - serverChatIds.toSet()

        chatsWithParticipants.forEach { chatWithParticipants ->
            chatWithParticipants.lastMessage?.run {
                messageDao.upsertMessage(
                    ChatMessageEntity(
                        messageId = messageId,
                        chatId = chatId,
                        senderId = senderId,
                        content = content,
                        timestamp = timestamp,
                        deliveryStatus = deliveryStatus
                    )
                )
            }
        }

        // this probably needs to be converted to a set (eliminating duplicates)
        val participants = chatsWithParticipants.flatMap { it.participants }
        participantDao.upsertParticipants(participants)

        val crossRefs = chatsWithParticipants.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participant ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.chatId,
                    userId = participant.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(crossRefs)

        chatsWithParticipants.forEach { chatWithParticipants ->
            crossRefDao.syncChatParticipants(
                chatId = chatWithParticipants.chat.chatId,
                participants = participants
            )
        }

        //Delete chats that differ from client side db
        deleteChatsByIds(staleChatIds)
    }
}