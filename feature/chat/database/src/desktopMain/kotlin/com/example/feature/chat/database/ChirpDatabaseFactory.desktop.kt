package com.example.feature.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core.data.util.applicationDirectory
import java.io.File

actual class ChirpDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        if (!applicationDirectory.exists()) {
            applicationDirectory.mkdirs()
        }

        val databaseFile = File(applicationDirectory, ChirpChatDatabase.DB_NAME)
        return Room.databaseBuilder(databaseFile.absolutePath)
    }
}