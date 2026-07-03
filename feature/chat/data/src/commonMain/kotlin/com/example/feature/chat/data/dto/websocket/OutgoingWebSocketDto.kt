package com.example.feature.chat.data.dto.websocket

import kotlinx.serialization.Serializable

@Serializable
sealed class OutgoingWebSocketDto(
    val type: OutgoingWebSocketType
) {

    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String
    ) : OutgoingWebSocketDto(type = OutgoingWebSocketType.NEW_MESSAGE)
}