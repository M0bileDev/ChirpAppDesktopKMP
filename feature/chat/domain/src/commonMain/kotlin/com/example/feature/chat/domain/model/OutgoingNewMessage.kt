package com.example.feature.chat.domain.model

data class OutgoingNewMessage(
    val chatId: String,
    val messageId: String,
    val content: String
)
