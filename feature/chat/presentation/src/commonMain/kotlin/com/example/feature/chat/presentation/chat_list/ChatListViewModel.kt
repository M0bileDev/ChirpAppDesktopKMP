package com.example.feature.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.auth.AuthService
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.presentation.ext.toUiText
import com.example.feature.chat.domain.chat.ChatRepository
import com.example.feature.chat.domain.notification.DeviceTokenService
import com.example.feature.chat.domain.participant.ChatParticipantRepository
import com.example.feature.chat.presentation.mappers.toUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository
) : ViewModel() {
    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        chatRepository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->
        if (authInfo == null) return@combine ChatListState()

        currentState.copy(
            chats = chats.map { it.toUi(authInfo.user.id) },
            localParticipant = authInfo.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                fetchLocalUserProfile()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatListState()
        )

    private fun fetchLocalUserProfile() {
        viewModelScope.launch {
            chatParticipantRepository
                .fetchLocalParticipant()
        }
    }

    private fun loadChats() = with(viewModelScope) {
        launch {
            chatRepository.fetchChats()
        }
    }

    fun onAction(chatListAction: ChatListAction) {
        when (chatListAction) {
            is ChatListAction.OnSelectChat -> {
                _state.update {
                    it.copy(
                        selectedChatId = chatListAction.chatId
                    )
                }
            }

            ChatListAction.OnUserAvatarClick -> {
                _state.update {
                    it.copy(
                        isUserManuOpen = true
                    )
                }
            }

            ChatListAction.OnLogoutClick -> showLogoutConfirmation()
            ChatListAction.OnConfirmLogout -> logout()
            ChatListAction.OnProfileSettingsClick,
            ChatListAction.OnDismissUserMenu -> {
                _state.update {
                    it.copy(
                        isUserManuOpen = false
                    )
                }
            }

            ChatListAction.OnDismissLogoutDialog -> {
                _state.update {
                    it.copy(
                        showLogoutConfirmation = false
                    )
                }
            }

            else -> Unit
        }
    }

    private fun showLogoutConfirmation() {
        _state.update {
            it.copy(
                showLogoutConfirmation = true
            )
        }
    }

    private fun logout() {
        _state.update {
            it.copy(
                showLogoutConfirmation = false
            )
        }

        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch

            deviceTokenService
                .unregisterToken(
                    token = refreshToken
                ).onSuccess {
                    authService.logout(
                        refreshToken = refreshToken
                    ).onSuccess {
                        sessionStorage.set(null)
                        chatRepository.deleteAllChats()
                        eventChannel.send(ChatListEvent.OnLogoutSuccess)
                    }.onFailure { error ->
                        eventChannel.send(ChatListEvent.OnLogoutError(error = error.toUiText()))
                    }
                }.onFailure { error ->
                    eventChannel.send(ChatListEvent.OnLogoutError(error = error.toUiText()))
                }
        }
    }
}
