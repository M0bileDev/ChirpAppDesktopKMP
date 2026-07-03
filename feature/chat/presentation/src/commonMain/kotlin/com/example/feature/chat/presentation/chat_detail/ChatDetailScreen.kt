@file:OptIn(ExperimentalUuidApi::class, ExperimentalComposeUiApi::class)

package com.example.feature.chat.presentation.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.no_chat_selected
import chirpappkmp.feature.chat.presentation.generated.resources.select_a_chat
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.composableUtil.currentDeviceConfiguration
import com.example.core.presentation.util.ObserveAsEvents
import com.example.core.presentation.util.UiText
import com.example.core.presentation.util.clearFocusOnTap
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.presentation.chat_detail.components.ChatDetailHeader
import com.example.feature.chat.presentation.chat_detail.components.DateChip
import com.example.feature.chat.presentation.chat_detail.components.MessageBannerScrollListener
import com.example.feature.chat.presentation.chat_detail.components.MessageBox
import com.example.feature.chat.presentation.chat_detail.components.MessageList
import com.example.feature.chat.presentation.chat_detail.components.PaginationScrollListener
import com.example.feature.chat.presentation.components.ChatHeader
import com.example.feature.chat.presentation.components.EmptySection
import com.example.feature.chat.presentation.model.ChatUi
import com.example.feature.chat.presentation.model.MessageUi
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

const val FIRST_LIST_ITEM = 0

@Composable
fun ChatDetailRoot(
    chatId: String?,
    isDetailPresent: Boolean,
    onBack: () -> Unit,
    onChatMembersClick: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }
    val messageLazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val realMessageCount = remember(state.messages) {
        state.messages.filter {
            it is MessageUi.LocalUserMessage || it is MessageUi.OtherUserMessage
        }.size
    }
    PaginationScrollListener(
        lazyListState = messageLazyListState,
        itemsCount = realMessageCount,
        isPaginationLoading = state.isPaginationLoading,
        isEndReached = state.endReached,
        onNearTop = {
            viewModel.onAction(ChatDetailAction.OnScrollToTop)
        }
    )

    MessageBannerScrollListener(
        lazyListState = messageLazyListState,
        messages = state.messages,
        isBannerVisible = state.bannerState.isVisible,
        onShowBanner = { topVisibleIndex ->
            viewModel.onAction(ChatDetailAction.OnMessagesScrollTopIndexChanged(topVisibleIndex = topVisibleIndex))
        },
        onHideBanner = {
            viewModel.onAction(ChatDetailAction.OnHideMessagesBanner)
        }
    )

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ChatDetailEvent.OnChatLeft -> onBack()
            is ChatDetailEvent.OnError -> {
                snackbarState.showSnackbar(event.error.asStringAsync())
            }

            ChatDetailEvent.OnNewMessage -> {
                messageLazyListState.animateScrollToItem(FIRST_LIST_ITEM)
            }
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    LaunchedEffect(chatId, state.messages) {
        if (state.messages.isNotEmpty()) {
            messageLazyListState.scrollToItem(0)
        }
    }

    LaunchedEffect(messageLazyListState) {
        snapshotFlow {
            messageLazyListState.firstVisibleItemIndex to messageLazyListState.layoutInfo.totalItemsCount
        }.filter { (firstVisibleIndex, totalItemsCount) ->
            firstVisibleIndex >= 0 && totalItemsCount > 0
        }.collect { (firstVisibleIndex, _) ->
            viewModel.onAction(ChatDetailAction.OnMessagesScrollFirstIndexChanged(firstVisibleIndex))
        }
    }

    BackHandler(
        enabled = !isDetailPresent
    ) {
        scope.launch {
            delay(300L)
            viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        }
        onBack()
    }

    ChatDetailScreen(
        state = state,
        snackbarState = snackbarState,
        isDetailPresent = isDetailPresent,
        lazyListState = messageLazyListState,
        onAction = { action ->
            when (action) {
                is ChatDetailAction.OnChatMembersClick -> onChatMembersClick()
                is ChatDetailAction.OnBackClick -> onBack()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    snackbarState: SnackbarHostState,
    isDetailPresent: Boolean,
    lazyListState: LazyListState,
    onAction: (ChatDetailAction) -> Unit
) = with(state) {
    val configuration = currentDeviceConfiguration()
    val containerColor = if (!configuration.isWideScreen) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.extended.surfaceLower
    }
    var headerHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = containerColor,
        snackbarHost = {
            SnackbarHost(snackbarState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    isCornersRounded = configuration.isWideScreen
                ) {
                    if (chatUi == null) {
                        EmptySection(
                            modifier = Modifier.fillMaxSize(),
                            title = stringResource(Res.string.no_chat_selected),
                            description = stringResource(Res.string.select_a_chat),
                        )
                    } else {
                        ChatHeader(
                            modifier = Modifier.onSizeChanged {
                                headerHeight = with(density) {
                                    it.height.toDp()
                                }
                            }
                        ) {
                            ChatDetailHeader(
                                modifier = Modifier.fillMaxWidth(),
                                chatUi = chatUi,
                                isDetailPresent = isDetailPresent,
                                isChatOptionsDropDownOpen = isChatOptionsOpen,
                                onChatOptionsClick = {
                                    onAction(ChatDetailAction.OnChatOptionsClick)
                                },
                                onDismissChatOptions = {
                                    onAction(ChatDetailAction.OnDismissChatOptions)
                                },
                                onManageChatClick = {
                                    onAction(ChatDetailAction.OnChatMembersClick)
                                },
                                onLeaveChatClick = {
                                    onAction(ChatDetailAction.OnLeaveChatClick)
                                },
                                onBackClick = {
                                    onAction(ChatDetailAction.OnBackClick)
                                }
                            )
                        }
                        MessageList(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            messages = messages,
                            messageWithOpenMenu = state.messageWitOpenMenu,
                            listState = lazyListState,
                            paginationError = paginationError?.asString(),
                            isPaginationLoading = isPaginationLoading,
                            onMessageLongClick = { message ->
                                onAction(ChatDetailAction.OnMessageLongClick(message))
                            },
                            onMessageRetryClick = { message ->
                                onAction(ChatDetailAction.OnRetryClick(message))
                            },
                            onDismissMessageMenu = {
                                onAction(ChatDetailAction.OnDismissMessageMenu)
                            },
                            onDeleteMessageClick = { message ->
                                onAction(ChatDetailAction.OnDeleteMessageClick(message))
                            },
                            onPaginationRetryClick = {
                                onAction(ChatDetailAction.OnPaginationRetryClick)
                            }
                        )

                        AnimatedVisibility(
                            visible = !configuration.isWideScreen
                        ) {
                            MessageBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .imePadding()
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    ),
                                messageTextFieldState = messageTextFieldState,
                                isSendButtonEnabled = canSendMessage,
                                connectionState = connectionState,
                                onSendClick = {
                                    onAction(ChatDetailAction.OnSendMessageClick)
                                }
                            )
                        }

                    }
                }

                if (configuration.isWideScreen) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && chatUi != null
                ) {
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen
                    ) {
                        MessageBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .imePadding()
                                .padding(8.dp),
                            messageTextFieldState = messageTextFieldState,
                            isSendButtonEnabled = canSendMessage,
                            connectionState = connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            }
                        )
                    }
                }
            }

            AnimatedDateChip(bannerState, headerHeight)
        }
    }
}

@Composable
private fun BoxScope.AnimatedDateChip(
    bannerState: BannerState,
    headerHeight: Dp
) = with(bannerState) {
    AnimatedVisibility(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = headerHeight + 16.dp),
        visible = isVisible
    ) {
        if (formattedDate != null) {
            DateChip(
                date = formattedDate.asString()
            )
        }
    }
}

@Composable
private fun DynamicRoundedCornerColumn(
    isCornersRounded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape

    Column(
        modifier = modifier
            .shadow(
                elevation = if (isCornersRounded) 8.dp else 0.dp,
                shape = shape,
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
    ) {
        content()
    }
}

@Preview
@Composable
fun PreviewChatDetailScreen() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            snackbarState = SnackbarHostState(),
            isDetailPresent = false,
            lazyListState = rememberLazyListState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewChatDetailScreenMessages() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = TextFieldState(
                    initialText = "New message"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = Uuid.random().toString(),
                    localParticipant = ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUi(
                            id = Uuid.random().toString(),
                            username = "Lorem ipsum1",
                            initials = "LI1"
                        )
                    ),
                    lastMessage = null,
                    lastMessageSenderUsername = null
                ),
                messages = (1..20).map {
                    val showLocalMessage = Random.nextBoolean()
                    if (showLocalMessage) {
                        MessageUi.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Lorem ipsum",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                        )
                    } else {
                        MessageUi.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Lorem ipsum",
                            formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "Lorem ipsum 1",
                                initials = "LI1"
                            )
                        )
                    }
                }
            ),
            snackbarState = SnackbarHostState(),
            isDetailPresent = true,
            lazyListState = rememberLazyListState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatDetailScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatDetailScreen(
            state = ChatDetailState(),
            snackbarState = SnackbarHostState(),
            isDetailPresent = false,
            lazyListState = rememberLazyListState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatDetailScreenMessages() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = TextFieldState(
                    initialText = "New message"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = Uuid.random().toString(),
                    localParticipant = ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUi(
                            id = Uuid.random().toString(),
                            username = "Lorem ipsum1",
                            initials = "LI1"
                        )
                    ),
                    lastMessage = null,
                    lastMessageSenderUsername = null
                ),
                messages = (1..20).map {
                    val showLocalMessage = Random.nextBoolean()
                    if (showLocalMessage) {
                        MessageUi.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Lorem ipsum",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                        )
                    } else {
                        MessageUi.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Lorem ipsum",
                            formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "Lorem ipsum 1",
                                initials = "LI1"
                            )
                        )
                    }
                }
            ),
            snackbarState = SnackbarHostState(),
            isDetailPresent = true,
            lazyListState = rememberLazyListState(),
            onAction = {}
        )
    }
}