package com.example.feature.chat.presentation.chat_list_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.feature.chat.domain.chat.ChatConnectionClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel(
    private val connectionClient: ChatConnectionClient
) : ViewModel() {

    private var hasLoadedInitialData = false

    private var _state = MutableStateFlow(ChatListDetailState())
    val state =
        _state.onStart {
            if (!hasLoadedInitialData) {
                connectionClient.chatMessages.launchIn(viewModelScope)
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatListDetailState()
        )

    fun onAction(action: ChatListDetailAction) {
        when (action) {
            is ChatListDetailAction.OnSelectChat -> chatClick(action.chatId)
            ChatListDetailAction.OnCreateChatClick -> createChat()
            ChatListDetailAction.OnDismissCurrentDialog -> dismissDialog()
            ChatListDetailAction.OnManageChatClick -> manageChat()
            ChatListDetailAction.OnProfileSettingsClick -> profileSettings()
        }
    }

    private fun chatClick(selectedChatId: String?) {
        _state.update {
            it.copy(
                selectedChatId = selectedChatId
            )
        }
    }

    private fun createChat() {
        _state.update {
            it.copy(
                dialogState = DialogState.CreateChat
            )
        }
    }

    private fun dismissDialog() {
        _state.update {
            it.copy(
                dialogState = DialogState.Hidden
            )
        }
    }

    private fun manageChat() {
        state.value.selectedChatId?.let { chatId ->
            _state.update {
                it.copy(
                    dialogState = DialogState.ManageChat(chatId)
                )
            }
        }
    }

    private fun profileSettings() {
        _state.update {
            it.copy(
                dialogState = DialogState.Profile
            )
        }
    }
}