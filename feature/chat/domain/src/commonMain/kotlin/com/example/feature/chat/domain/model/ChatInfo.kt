package com.example.feature.chat.domain.model

data class ChatInfo(
    val chat: Chat,
    val messagesWithSenders: List<MessageWithSender>
)