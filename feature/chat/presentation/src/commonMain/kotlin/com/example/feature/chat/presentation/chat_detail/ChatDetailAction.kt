package com.example.feature.chat.presentation.chat_detail

import com.example.feature.chat.presentation.model.MessageUi

sealed interface ChatDetailAction {
    data object OnSendMessageClick : ChatDetailAction
    data object OnScrollToTop : ChatDetailAction
    data class OnSelectChat(val chatId: String?) : ChatDetailAction
    data class OnDeleteMessageClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
    data class OnMessageLongClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
    data object OnDismissMessageMenu : ChatDetailAction
    data class OnRetryClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
    data object OnBackClick : ChatDetailAction
    data object OnChatOptionsClick : ChatDetailAction
    data object OnChatMembersClick : ChatDetailAction
    data object OnLeaveChatClick : ChatDetailAction
    data object OnDismissChatOptions : ChatDetailAction
    data object OnPaginationRetryClick : ChatDetailAction
    data class OnMessagesScrollTopIndexChanged(val topVisibleIndex: Int) : ChatDetailAction
    data object OnHideMessagesBanner : ChatDetailAction
    data class OnMessagesScrollFirstIndexChanged(val firstVisibleIndex: Int) : ChatDetailAction

}