@file:OptIn(ExperimentalForeignApi::class)

package com.example.feature.chat.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class ChirpDatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChirpChatDatabase> {
        val databaseFile = documentDirectory() + "/${ChirpChatDatabase.DB_NAME}"
        return Room.databaseBuilder(
            name = databaseFile
        )
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )

        return requireNotNull(documentDirectory?.path)
    }
}