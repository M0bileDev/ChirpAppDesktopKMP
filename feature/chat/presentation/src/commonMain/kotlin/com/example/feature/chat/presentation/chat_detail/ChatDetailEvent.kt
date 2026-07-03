package com.example.feature.chat.presentation.chat_detail

import com.example.core.presentation.util.UiText

sealed interface ChatDetailEvent {
    data object OnChatLeft : ChatDetailEvent
    data class OnError(val error: UiText) : ChatDetailEvent
    data object OnNewMessage : ChatDetailEvent
}