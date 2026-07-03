package com.example.feature.chat.presentation.chat_list

import com.example.core.presentation.util.UiText

interface ChatListEvent {
    data object OnLogoutSuccess : ChatListEvent
    data class OnLogoutError(val error: UiText) : ChatListEvent
}