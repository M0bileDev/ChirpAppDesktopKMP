package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.group_chat
import chirpappkmp.feature.chat.presentation.generated.resources.only_you
import chirpappkmp.feature.chat.presentation.generated.resources.you
import com.example.core.designsystem.components.avatar.ChirpStackedAvatars
import com.example.core.designsystem.theme.extended
import com.example.core.designsystem.theme.titleXSmall
import com.example.feature.chat.presentation.model.ChatUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatItemHeaderRow(
    chatUi: ChatUi,
    isGroupChat: Boolean,
    modifier: Modifier = Modifier
) = with(chatUi) {

    val chatName = if (isGroupChat) stringResource(Res.string.group_chat)
    else otherParticipants.firstOrNull()?.username ?: stringResource(Res.string.only_you)
    val you = stringResource(Res.string.you)
    val formattedUsernames = remember(otherParticipants) {
        "$you, " + otherParticipants.joinToString { it.username }
    }
    val chatParticipantsNotEmpty = otherParticipants.isNotEmpty()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (chatParticipantsNotEmpty) {
            ChirpStackedAvatars(
                avatars = otherParticipants
            )
        }
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = chatName,
                style = MaterialTheme.typography.titleXSmall,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if (isGroupChat) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = formattedUsernames,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textPlaceholder,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }
    }
}