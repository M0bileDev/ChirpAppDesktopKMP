@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.feature.chat.domain.model.ChatMessage
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.presentation.chat_detail.components.ChatItemHeaderRow
import com.example.feature.chat.presentation.model.ChatUi
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ChatListItem(
    chatUi: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) = with(chatUi) {
    val selectedColor =
        if (isSelected) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.extended.surfaceLower
    val selectedAlpha = if (isSelected) 1f else 0f
    val isGroupChat = otherParticipants.size > 1

    val previewMessage = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
        ) {
            if(lastMessageSenderUsername != null){
                append("$lastMessageSenderUsername: ")
            }
        }
        append(lastMessage?.content)
    }

    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(selectedColor)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatItemHeaderRow(
                modifier = Modifier.fillMaxWidth(),
                chatUi = chatUi,
                isGroupChat = isGroupChat
            )
            lastMessage?.let {
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(selectedAlpha)
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}

@Preview
@Composable
fun PreviewChatListItem() {
    ChirpTheme {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatListItem() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewChatListItemGroup() {
    ChirpTheme {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 3",
                        initials = "LI3"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatListItemGroup() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 3",
                        initials = "LI3"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewChatListItemGroupLastMessage() {
    ChirpTheme {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 3",
                        initials = "LI3"
                    )
                ),
                lastMessage = ChatMessage(
                    id = Uuid.random().toString(),
                    chatId = Uuid.random().toString(),
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    createdAt = Clock.System.now(),
                    senderId = Uuid.random().toString(),
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                lastMessageSenderUsername = "Lorem ipsum"
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatListItemGroupLastMessage() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 3",
                        initials = "LI3"
                    )
                ),
                lastMessage = ChatMessage(
                    id = Uuid.random().toString(),
                    chatId = Uuid.random().toString(),
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    createdAt = Clock.System.now(),
                    senderId = Uuid.random().toString(),
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                lastMessageSenderUsername = "Lorem ipsum"
            ),
            isSelected = false,
        )
    }
}

@Preview
@Composable
fun PreviewChatListItemSelected() {
    ChirpTheme {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = true,
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatListItemSelected() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListItem(
            chatUi = ChatUi(
                id = Uuid.random().toString(),
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 1",
                        initials = "LI1"
                    )
                ),
                lastMessage = null,
                lastMessageSenderUsername = null
            ),
            isSelected = true,
        )
    }
}