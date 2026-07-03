package com.example.feature.chat.presentation.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.example.core.presentation.util.UiText
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi

data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val isSubmitting: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val submitError: UiText? = null
)