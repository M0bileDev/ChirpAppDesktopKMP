package com.example.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.feature.chat.database.entities.ChatMessageEntity
import com.example.feature.chat.database.entities.MessageWithSender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Dao
interface ChatMessageDao {

    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    @Query("DELETE FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    @Query("DELETE FROM chatmessageentity WHERE messageId IN (:messageIds)")
    suspend fun deleteMessagesByIds(messageIds: List<String>)

    @Transaction
    @Query("SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesWithSenderByChatId(chatId: String): Flow<List<MessageWithSender>>

    @Query(
        """
        SELECT * 
        FROM chatmessageentity   
        WHERE chatId = :chatId 
        ORDER BY timestamp DESC
        LIMIT :limit
    """
    )
    fun getMessagesByChatIdLimited(chatId: String, limit: Int): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    @Query(
        """
        UPDATE chatmessageentity
        SET deliveryStatus = :status, deliveryStatusTimeStamp = :timestamp
        WHERE messageId = :messageId
    """
    )
    suspend fun updateDeliveryStatus(messageId: String, status: String, timestamp: Long)

    @Transaction
    suspend fun upsertMessagesAndSyncIfNecessary(
        chatId: String,
        serverMessages: List<ChatMessageEntity>,
        pageSize: Int,
        shouldSync: Boolean = false
    ) {
        upsertMessages(messages = serverMessages)
        if (!shouldSync) return

        val localMessages = getMessagesByChatIdLimited(
            chatId = chatId,
            limit = pageSize
        ).firstOrNull()
        if (localMessages == null) return

        val serverIds = serverMessages.map { it.messageId }.toSet()
        val messagesToDelete = localMessages.filter { localMessage ->
            val missingOnServer = localMessage.messageId !in serverIds
            val isSent = localMessage.deliveryStatus == "SENT"

            missingOnServer && isSent
        }

        val messageByIds = messagesToDelete.map { it.messageId }
        deleteMessagesByIds(messageIds = messageByIds)
    }
}