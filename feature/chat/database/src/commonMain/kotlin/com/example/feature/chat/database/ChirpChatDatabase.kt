package com.example.feature.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.feature.chat.database.dao.ChatDao
import com.example.feature.chat.database.dao.ChatMessageDao
import com.example.feature.chat.database.dao.ChatParticipantDao
import com.example.feature.chat.database.dao.ChatParticipantsCrossRefDao
import com.example.feature.chat.database.entities.ChatEntity
import com.example.feature.chat.database.entities.ChatMessageEntity
import com.example.feature.chat.database.entities.ChatParticipantCrossRef
import com.example.feature.chat.database.entities.ChatParticipantEntity
import com.example.feature.chat.database.view.LastMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class
    ],
    views = [
        LastMessageView::class
    ],
    version = 1
)
@ConstructedBy(ChirpChatDatabaseConstructor::class)
abstract class ChirpChatDatabase : RoomDatabase() {

    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "chirp.db"
    }
}