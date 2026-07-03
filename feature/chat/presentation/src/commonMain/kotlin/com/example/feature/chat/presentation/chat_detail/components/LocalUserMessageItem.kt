@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.delete_for_everyone
import chirpappkmp.feature.chat.presentation.generated.resources.reload_icon
import chirpappkmp.feature.chat.presentation.generated.resources.retry
import chirpappkmp.feature.chat.presentation.generated.resources.you
import com.example.core.designsystem.components.chat.ChirpChatBubble
import com.example.core.designsystem.components.chat.TrianglePosition
import com.example.core.designsystem.components.dropdown.ChirpDropDownMenu
import com.example.core.designsystem.components.dropdown.DropDownItem
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import com.example.feature.chat.presentation.model.MessageUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun LocalUserMessageItem(
    messageUi: MessageUi.LocalUserMessage,
    messageWithOpenMenu: MessageUi.LocalUserMessage? = null,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) = with(messageUi) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        Box {
            ChirpChatBubble(
                messageContent = content,
                sender = stringResource(Res.string.you),
                formattedDateTime = formattedSentAt.asString(),
                trianglePosition = TrianglePosition.RIGHT,
                messageStatus = {
                    MessageStatus(
                        status = deliveryStatus
                    )
                },
                onLongClick = onMessageLongClick
            )

            ChirpDropDownMenu(
                isOpen = messageWithOpenMenu?.id == id,
                onDismiss = onDismissMessageMenu,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.delete_for_everyone),
                        icon = Icons.Default.Delete,
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onDeleteClick
                    )
                )
            )
        }

        if (deliveryStatus == ChatMessageDeliveryStatus.FAILED) {
            IconButton(
                onClick = onRetryClick
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.reload_icon),
                    contentDescription = stringResource(Res.string.retry),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewLocalUserMessageItem() {
    ChirpTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LocalUserMessageItem(
                messageUi = MessageUi.LocalUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                    formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                ),
                onMessageLongClick = {},
                onDismissMessageMenu = {},
                onDeleteClick = {},
                onRetryClick = {},
            )
        }

    }
}

@Preview
@Composable
fun PreviewDarkLocalUserMessageItem() {
    ChirpTheme(
        darkTheme = true
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LocalUserMessageItem(
                messageUi = MessageUi.LocalUserMessage(
                    id = Uuid.random().toString(),
                    content = "Lorem ipsum",
                    deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                    formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                ),
                onMessageLongClick = {},
                onDismissMessageMenu = {},
                onDeleteClick = {},
                onRetryClick = {},
            )
        }

    }
}