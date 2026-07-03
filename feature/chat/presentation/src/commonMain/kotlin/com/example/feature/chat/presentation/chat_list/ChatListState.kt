package com.example.feature.chat.presentation.chat_list

import com.example.core.presentation.util.UiText
import com.example.feature.chat.presentation.model.ChatUi
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi

data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ChatParticipantUi? = null,
    val isUserManuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false
)