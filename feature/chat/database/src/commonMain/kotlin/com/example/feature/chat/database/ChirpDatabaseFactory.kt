package com.example.feature.chat.database

import androidx.room.RoomDatabase

expect class ChirpDatabaseFactory {
    fun create(): RoomDatabase.Builder<ChirpChatDatabase>
}