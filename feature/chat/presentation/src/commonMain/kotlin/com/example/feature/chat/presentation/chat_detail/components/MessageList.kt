@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.no_messages
import chirpappkmp.feature.chat.presentation.generated.resources.no_messages_subtitle
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.presentation.components.EmptySection
import com.example.feature.chat.presentation.model.MessageUi
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun MessageList(
    messages: List<MessageUi>,
    paginationError: String?,
    isPaginationLoading: Boolean,
    listState: LazyListState,
    messageWithOpenMenu: MessageUi.LocalUserMessage? = null,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
    onMessageRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    onPaginationRetryClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteMessageClick: (MessageUi.LocalUserMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            EmptySection(
                title = stringResource(Res.string.no_messages),
                description = stringResource(Res.string.no_messages_subtitle)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(16.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageListItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    messageUi = message,
                    messageWithOpenMenu = messageWithOpenMenu,
                    onMessageLongClick = onMessageLongClick,
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = onDeleteMessageClick,
                    onRetryClick = onMessageRetryClick
                )
            }

            when {
                isPaginationLoading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                paginationError != null -> {
                    item {
                        PaginationErrorRetryItem(
                            paginationErrorText = paginationError,
                            onPaginationRetryClick = onPaginationRetryClick
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewMessageList() {
    ChirpTheme {
        MessageList(
            messages = listOf(
                MessageUi.LocalUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    formattedSentAt = UiText.DynamicString("01/01/1990 00:00"),
                ),
                MessageUi.DateSeparator(
                    id = Uuid.random().toString(),
                    date = UiText.DynamicString("01/01/1990 00:00")
                ),
                MessageUi.OtherUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    formattedSentAt = UiText.DynamicString("01/01/1990 00:00"),
                    sender = ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1",
                        imageUrl = null
                    )
                ),
            ),
            listState = rememberLazyListState(),
            onMessageLongClick = {},
            onMessageRetryClick = {},
            onDismissMessageMenu = {},
            onDeleteMessageClick = {},
            onPaginationRetryClick = {},
            paginationError = "Lorem ipsum",
            isPaginationLoading = false
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageList() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageList(
            messages = listOf(
                MessageUi.LocalUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    formattedSentAt = UiText.DynamicString("01/01/1990 00:00"),
                ),
                MessageUi.DateSeparator(
                    id = Uuid.random().toString(),
                    date = UiText.DynamicString("01/01/1990 00:00")
                ),
                MessageUi.OtherUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    formattedSentAt = UiText.DynamicString("01/01/1990 00:00"),
                    sender = ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1",
                        imageUrl = null
                    )
                ),
            ),
            listState = rememberLazyListState(),
            onMessageLongClick = {},
            onMessageRetryClick = {},
            onDismissMessageMenu = {},
            onDeleteMessageClick = {},
            onPaginationRetryClick = {},
            paginationError = null,
            isPaginationLoading = true
        )
    }
}