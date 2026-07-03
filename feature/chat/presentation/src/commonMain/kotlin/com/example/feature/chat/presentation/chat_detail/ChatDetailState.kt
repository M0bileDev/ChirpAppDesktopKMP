package com.example.feature.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.model.ConnectionState
import com.example.feature.chat.presentation.model.ChatUi
import com.example.feature.chat.presentation.model.MessageUi

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val messages: List<MessageUi> = emptyList(),
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isChatLoading: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val error: UiText? = null,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED,
    val messageWitOpenMenu: MessageUi.LocalUserMessage? = null,
)

data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false
)