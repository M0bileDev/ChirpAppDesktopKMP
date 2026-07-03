package com.example.feature.chat.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class ChirpDatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> = with(context) {
        val databaseFile = applicationContext.getDatabasePath(ChirpChatDatabase.DB_NAME)
        return@with Room.databaseBuilder(
            applicationContext,
            databaseFile.absolutePath
        )
    }
}