package com.example.feature.chat.data.chat

import com.example.core.data.network.delete
import com.example.core.data.network.get
import com.example.core.data.network.post
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.asEmptyResult
import com.example.core.domain.util.map
import com.example.feature.chat.data.dto.ChatDto
import com.example.feature.chat.data.dto.request.CreateChatRequest
import com.example.feature.chat.data.dto.request.ParticipantsRequest
import com.example.feature.chat.data.mappers.toDomain
import com.example.feature.chat.domain.chat.ChatService
import com.example.feature.chat.domain.model.Chat
import io.ktor.client.HttpClient

class KtorChatService(
    val httpClient: HttpClient
) : ChatService {
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map {
            it.toDomain()
        }
    }

    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDto ->
            chatDto.map { it.toDomain() }
        }
    }

    override suspend fun getChatById(chatId: String): Result<Chat, DataError.Remote> {
        return httpClient.get<ChatDto>(
            route = "/chat/$chatId"
        ).map { it.toDomain() }
    }

    override suspend fun leaveChat(chatId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete<Unit>(
            route = "/chat/$chatId/leave"
        ).asEmptyResult()
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>
    ): Result<Chat, DataError.Remote> {
        return httpClient.post<ParticipantsRequest, ChatDto>(
            route = "/chat/$chatId/add",
            body = ParticipantsRequest(
                userIds = userIds
            )
        ).map { it.toDomain() }
    }
}