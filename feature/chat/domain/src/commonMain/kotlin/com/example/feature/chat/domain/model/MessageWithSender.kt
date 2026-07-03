package com.example.feature.chat.domain.model

data class MessageWithSender(
    val message: ChatMessage,
    val sender: ChatParticipant,
    val status: ChatMessageDeliveryStatus?
)