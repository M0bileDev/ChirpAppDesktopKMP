package com.example.feature.chat.presentation.model

import com.example.feature.chat.domain.model.ChatMessage
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)