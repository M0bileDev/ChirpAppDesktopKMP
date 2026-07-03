package com.example.feature.chat.presentation.create_chat

import com.example.feature.chat.domain.model.Chat

sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat) : CreateChatEvent
}