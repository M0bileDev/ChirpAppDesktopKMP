@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.today
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.PaginationErrorException
import com.example.core.domain.util.Paginator
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.presentation.ext.toUiText
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.chat.ChatConnectionClient
import com.example.feature.chat.domain.chat.ChatRepository
import com.example.feature.chat.domain.message.MessageRepository
import com.example.feature.chat.domain.model.ChatMessage
import com.example.feature.chat.domain.model.ConnectionState
import com.example.feature.chat.domain.model.MessageWithSender
import com.example.feature.chat.domain.model.OutgoingNewMessage
import com.example.feature.chat.presentation.mappers.toUi
import com.example.feature.chat.presentation.mappers.toUiList
import com.example.feature.chat.presentation.model.MessageUi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val messageRepository: MessageRepository,
    private val connectionClient: ChatConnectionClient
) : ViewModel() {

    private var chatMessagePaginator: Paginator<String?, ChatMessage>? = null
    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()
    private val _chatId = MutableStateFlow<String?>(null)
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(ChatDetailState())
    private val canSandMessage =
        snapshotFlow { _state.value.messageTextFieldState.text.toString() }
            .map { it.isBlank() }
            .combine(connectionClient.connectionState) { isMessageBlank, connectionState ->
                !isMessageBlank && connectionState == ConnectionState.CONNECTED
            }
    private val chatInfoFlow = _chatId
        .onEach { chatId ->
            if (chatId != null) {
                setupPaginatorForChat(chatId = chatId)
                loadPaginatorNextPage()
            } else {
                clearPaginator()
            }
        }
        .flatMapLatest { chatId ->
            if (chatId != null) {
                chatRepository.getChatInfoById(chatId)
            } else emptyFlow()
        }

    private val stateWithMessages = combine(
        _state,
        chatInfoFlow,
        sessionStorage.observeAuthInfo()
    ) { currentState, chatInfo, authInfo ->
        if (authInfo == null) return@combine ChatDetailState()

        currentState.copy(
            chatUi = chatInfo.chat.toUi(localParticipantId = authInfo.user.id),
            messages = chatInfo.messagesWithSenders.toUiList(authInfo.user.id)
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                stateWithMessages
            } else {
                _state
            }
        }
        .onStart {
            if (!hasLoadedInitialData) {
                observeConnectionState()
                observeNewMessage()
                observeMessages()
                observeCanSendMessage()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatDetailState()
        )

    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(action.chatId)
            ChatDetailAction.OnChatOptionsClick -> chatOptionsClick()
            ChatDetailAction.OnDismissChatOptions -> dismissChatOptions()
            ChatDetailAction.OnLeaveChatClick -> leaveChatClick()
            ChatDetailAction.OnSendMessageClick -> sendMessage()
            is ChatDetailAction.OnRetryClick -> retryMessage(action.message)
            is ChatDetailAction.OnDeleteMessageClick -> deleteMessage(action.message)
            ChatDetailAction.OnDismissMessageMenu -> dismissMessageMenu()
            is ChatDetailAction.OnMessageLongClick -> onMessageLongClick(action.message)
            ChatDetailAction.OnScrollToTop -> loadPaginatorNextPage()
            ChatDetailAction.OnPaginationRetryClick -> loadPaginatorNextPage()
            ChatDetailAction.OnHideMessagesBanner -> hideBanner()
            is ChatDetailAction.OnMessagesScrollTopIndexChanged -> updateBanner(action.topVisibleIndex)
            is ChatDetailAction.OnMessagesScrollFirstIndexChanged -> updateNearBottom(action.firstVisibleIndex)
            else -> Unit
        }
    }

    private fun switchChat(chatId: String?) = with(viewModelScope) {
        _chatId.update { chatId }
        launch {
            chatId?.let { id ->
                chatRepository.fetchChatById(id)
            }
        }
    }

    private fun chatOptionsClick() {
        _state.update {
            it.copy(
                isChatOptionsOpen = true
            )
        }
    }

    private fun dismissChatOptions() {
        _state.update {
            it.copy(
                isChatOptionsOpen = false
            )
        }
    }

    private fun leaveChatClick() = with(viewModelScope) {
        val chatId = _chatId.value ?: return@with

        _state.update {
            it.copy(
                isChatOptionsOpen = false
            )
        }

        launch {
            chatRepository
                .leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()
                    _chatId.update { null }
                    _state.update {
                        it.copy(
                            chatUi = null,
                            messages = emptyList(),
                            bannerState = BannerState()
                        )
                    }

                    eventChannel.send(
                        ChatDetailEvent.OnChatLeft
                    )
                }
                .onFailure { error ->
                    eventChannel.send(
                        ChatDetailEvent.OnError(
                            error.toUiText()
                        )
                    )
                }
        }
    }

    private fun sendMessage() = with(viewModelScope) {
        val currentChatId = _chatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()
        if (content.isBlank() || currentChatId == null) return@with

        launch {
            val message = OutgoingNewMessage(
                chatId = currentChatId,
                messageId = Uuid.random().toString(),
                content = content
            )

            messageRepository
                .sendMessage(message)
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun retryMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .retryMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun deleteMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .deleteMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun dismissMessageMenu() {
        _state.update {
            it.copy(
                messageWitOpenMenu = null
            )
        }
    }

    private fun onMessageLongClick(message: MessageUi.LocalUserMessage) {
        _state.update {
            it.copy(
                messageWitOpenMenu = message
            )
        }
    }

    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if (connectionState == ConnectionState.CONNECTED) {
                    //before == null -> load most recent page of messages
                    chatMessagePaginator?.loadNextItems()
                }

                _state.update {
                    it.copy(
                        connectionState = connectionState
                    )
                }

            }.launchIn(viewModelScope)
    }

    private fun observeNewMessage() {
        val currentMessages = state
            .map { it.messages }
            .distinctUntilChanged()

        val newMessages = getNewMessagesWithSenderByChatIdFlow()

        val isNearBottom = state.map { it.isNearBottom }.distinctUntilChanged()

        combine(
            currentMessages,
            newMessages,
            isNearBottom
        ) { currentMessages, newMessages, isNearBottom ->
            val lastNewId = newMessages.lastOrNull()?.message?.id
            val lastCurrentId = currentMessages.lastOrNull()?.id

            if (lastNewId != lastCurrentId && isNearBottom) {
                eventChannel.send(ChatDetailEvent.OnNewMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeMessages() {
        getNewMessagesWithSenderByChatIdFlow()
            .combine(sessionStorage.observeAuthInfo())
            { messages, authInfo ->
                if (authInfo == null) return@combine

                _state.update {
                    it.copy(
                        messages = messages.map { message -> message.toUi(authInfo.user.id) }
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getNewMessagesWithSenderByChatIdFlow(): Flow<List<MessageWithSender>> {
        return _chatId
            .flatMapLatest { chatId ->
                if (chatId != null) {
                    messageRepository.getMessagesForChat(chatId)
                } else emptyFlow()
            }
    }

    private fun observeCanSendMessage() {
        canSandMessage
            .onEach { canSendMessage ->
                _state.update {
                    it.copy(
                        canSendMessage = canSendMessage
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun setupPaginatorForChat(chatId: String) {
        chatMessagePaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update {
                    it.copy(
                        isPaginationLoading = isLoading
                    )
                }
            },
            onRequest = { beforeTimeStamp ->
                messageRepository
                    .fetchMessages(chatId = chatId, before = beforeTimeStamp)
            },
            getNextKey = { messages ->
                messages.minOfOrNull { it.createdAt }?.toString()
            },
            onError = { throwable ->
                if (throwable is PaginationErrorException) {
                    _state.update {
                        it.copy(
                            paginationError = throwable.error.toUiText()
                        )
                    }
                }
            },
            onSuccess = { messages, _ ->
                _state.update {
                    it.copy(
                        endReached = messages.isEmpty(),
                        paginationError = null
                    )
                }
            }
        )
    }

    private fun loadPaginatorNextPage() =
        chatMessagePaginator?.let { paginator ->
            viewModelScope.launch {
                paginator.loadNextItems()
            }
        }

    private fun hideBanner() {
        _state.update {
            it.copy(
                bannerState = it.bannerState.copy(
                    isVisible = false
                )
            )
        }
    }

    private fun updateBanner(topVisibleIndex: Int) {
        val visibleDate = calculateBannerDateFromIndex(
            messages = state.value.messages,
            index = topVisibleIndex
        )

        _state.update {
            it.copy(
                bannerState = BannerState(
                    formattedDate = visibleDate,
                    isVisible = visibleDate != null
                )
            )
        }
    }

    private fun updateNearBottom(firstVisibleIndex: Int) {
        _state.update {
            it.copy(
                isNearBottom = firstVisibleIndex <= 3
            )
        }
    }

    private fun calculateBannerDateFromIndex(
        messages: List<MessageUi>,
        index: Int
    ): UiText? {
        if (messages.isEmpty() || index < 0 || index >= messages.size) return null

        val nearestDateSeparator = (index until messages.size).firstNotNullOfOrNull { index ->
            val item = messages.getOrNull(index)
            if (item is MessageUi.DateSeparator) item.date else null
        }

        return when (nearestDateSeparator) {
            is UiText.Resource -> {
                if (nearestDateSeparator.id == Res.string.today) null else nearestDateSeparator
            }

            else -> nearestDateSeparator
        }
    }

    private fun clearPaginator() {
        chatMessagePaginator = null
        _state.update {
            it.copy(
                endReached = false,
                isPaginationLoading = false
            )
        }
    }
}