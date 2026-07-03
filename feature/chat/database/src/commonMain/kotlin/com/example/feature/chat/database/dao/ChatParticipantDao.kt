package com.example.feature.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.feature.chat.database.entities.ChatParticipantEntity

@Dao
interface ChatParticipantDao {

    @Upsert
    suspend fun upsertParticipant(participant: ChatParticipantEntity)

    @Upsert
    suspend fun upsertParticipants(participants: List<ChatParticipantEntity>)

    @Query("SELECT * FROM chatparticipantentity")
    suspend fun getAllParticipants(): List<ChatParticipantEntity>

    @Query("""
            UPDATE chatparticipantentity
            SET profilePictureUrl = :newProfilePictureUrl
            WHERE userId = :userId
    """)
    suspend fun updateProfilePictureUrl(userId: String, newProfilePictureUrl: String?)
}