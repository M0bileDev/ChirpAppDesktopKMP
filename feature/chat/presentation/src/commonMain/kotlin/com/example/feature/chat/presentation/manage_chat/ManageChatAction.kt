package com.example.feature.chat.presentation.manage_chat

sealed interface ManageChatAction {
    data object OnAddClick : ManageChatAction
    data object OnDismissDialog : ManageChatAction
    data object OnPrimaryActionButtonClick : ManageChatAction

    sealed interface ChatParticipantsAction : ManageChatAction {
        data class OnSelectChat(val chatId: String?) : ManageChatAction
    }
}