@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.example.core.designsystem.components.chat.ChirpChatBubble
import com.example.core.designsystem.components.chat.TrianglePosition
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.util.UiText
import com.example.feature.chat.presentation.model.MessageUi
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun OtherUserMessageItem(
    messageUi: MessageUi.OtherUserMessage,
    color: Color = MaterialTheme.colorScheme.extended.surfaceHigher,
    modifier: Modifier = Modifier,
) = with(messageUi) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChirpAvatarPhoto(
            displayText = sender.initials,
            imageUrl = sender.imageUrl
        )
        ChirpChatBubble(
            messageContent = content,
            sender = sender.username,
            formattedDateTime = formattedSentAt.asString(),
            trianglePosition = TrianglePosition.LEFT,
            color = color
        )
    }
}

@Preview
@Composable
fun PreviewOtherUserMessageItem() {
    ChirpTheme {
        OtherUserMessageItem(
            messageUi = MessageUi.OtherUserMessage(
                id = Uuid.random().toString(),
                content = "Lorem ipsum",
                formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                sender = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                )
            )
        )
    }
}

@Preview
@Composable
fun PreviewDarkOtherUserMessageItem() {
    ChirpTheme(
        darkTheme = true
    ) {
        OtherUserMessageItem(
            messageUi = MessageUi.OtherUserMessage(
                id = Uuid.random().toString(),
                content = "Lorem ipsum",
                formattedSentAt = UiText.DynamicString("01/01/1900 00:00"),
                sender = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum 1",
                    initials = "LI1"
                )
            )
        )
    }
}