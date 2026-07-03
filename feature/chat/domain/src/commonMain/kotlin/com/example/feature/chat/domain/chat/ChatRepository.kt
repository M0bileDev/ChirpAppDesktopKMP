package com.example.feature.chat.domain.chat


import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.feature.chat.domain.model.Chat
import com.example.feature.chat.domain.model.ChatInfo
import com.example.feature.chat.domain.model.ChatParticipant
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getChats(): Flow<List<Chat>>
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
    suspend fun fetchChatById(chatId: String): EmptyResult<DataError.Remote>
    fun getChatInfoById(chatId: String): Flow<ChatInfo>
    suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote>
    suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote>
    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote>
    fun getActiveParticipantsByChatId(chatId: String):Flow<List<ChatParticipant>>
    suspend fun deleteAllChats()
}