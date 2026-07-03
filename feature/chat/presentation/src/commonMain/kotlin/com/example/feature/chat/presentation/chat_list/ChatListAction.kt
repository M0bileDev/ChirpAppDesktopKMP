package com.example.feature.chat.presentation.chat_list

import com.example.feature.chat.presentation.model.ChatUi

sealed interface ChatListAction {
    data object OnUserAvatarClick : ChatListAction
    data object OnDismissUserMenu : ChatListAction
    data object OnLogoutClick : ChatListAction
    data object OnProfileSettingsClick : ChatListAction
    data object OnConfirmLogout : ChatListAction
    data object OnDismissLogoutDialog : ChatListAction
    data object OnCreateChatClick : ChatListAction
    data class OnSelectChat(val chatId: String?) : ChatListAction
}