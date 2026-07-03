@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.core.designsystem.generated.resources.logo_chirp
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.feature.chat.presentation.components.ChatHeader
import com.example.feature.chat.presentation.components.ProfileAvatarSection
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import chirpappkmp.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ChatListHeader(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatHeader(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = vectorResource(DesignSystemRes.drawable.logo_chirp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Chirp",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

//There is some preview bug, use interactive mode
@Preview(showBackground = true)
@Composable
fun PreviewChatListHeader() {
    ChirpTheme {
        ChatListHeader(
            modifier = Modifier.height(200.dp),
            localParticipant = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            ),
            isUserMenuOpen = remember { true },
            onUserAvatarClick = {},
            onDismissMenu = {},
            onProfileSettingsClick = {},
            onLogoutClick = {},
        )
    }
}

//There is some preview bug, use interactive mode
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkChatListHeader() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListHeader(
            modifier = Modifier.height(200.dp),
            localParticipant = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            ),
            isUserMenuOpen = true,
            onUserAvatarClick = {},
            onDismissMenu = {},
            onProfileSettingsClick = {},
            onLogoutClick = {},
        )
    }
}