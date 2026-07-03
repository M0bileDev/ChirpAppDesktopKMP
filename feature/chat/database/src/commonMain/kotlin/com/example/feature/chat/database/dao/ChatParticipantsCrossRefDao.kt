package com.example.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.feature.chat.database.entities.ChatParticipantCrossRef
import com.example.feature.chat.database.entities.ChatParticipantEntity

@Dao
interface ChatParticipantsCrossRefDao {

    @Upsert
    suspend fun upsertCrossRefs(crossRefs: List<ChatParticipantCrossRef>)

    @Query("SELECT userId FROM chatparticipantcrossref WHERE chatId = :chatId AND isActive = 1")
    suspend fun getActiveParticipantIdsByChat(chatId: String): List<String>

    @Query(
        """
        UPDATE chatparticipantcrossref
        SET isActive = 0
        WHERE chatId = :chatId AND userId IN (:userIds)
    """
    )
    suspend fun markParticipantsAsInactive(chatId: String, userIds: List<String>)

    @Query(
        """
        UPDATE chatparticipantcrossref
        SET isActive = 1
        WHERE chatId = :chatId AND userId IN (:userIds)
    """
    )
    suspend fun markParticipantsAsActive(chatId: String, userIds: List<String>)

    @Query("SELECT userId FROM chatparticipantcrossref WHERE chatId = :chatId")
    suspend fun getParticipantIdsByChat(chatId: String): List<String>

    @Transaction
    suspend fun syncChatParticipants(
        chatId: String,
        participants: List<ChatParticipantEntity>
    ) {
        if (participants.isEmpty()) return

        val serverParticipantIds = participants.map { it.userId }.toSet()
        val localParticipantIds = getParticipantIdsByChat(chatId).toSet()
        val activeLocalParticipantIds = getActiveParticipantIdsByChat(chatId).toSet()
        val inactiveLocalParticipantIds = localParticipantIds - activeLocalParticipantIds

        val participantsToReactivate = serverParticipantIds.intersect(inactiveLocalParticipantIds)
        val participantsToDeactivate = activeLocalParticipantIds - serverParticipantIds

        markParticipantsAsInactive(chatId, participantsToDeactivate.toList())
        markParticipantsAsActive(chatId, participantsToReactivate.toList())

        val completelyNewParticipantIds = serverParticipantIds - localParticipantIds
        val newCrossRefs = completelyNewParticipantIds.map { userId ->
            ChatParticipantCrossRef(
                chatId = chatId,
                userId = userId,
                isActive = true
            )
        }
        upsertCrossRefs(newCrossRefs)
    }
}