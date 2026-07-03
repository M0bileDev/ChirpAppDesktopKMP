@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.core.designsystem.generated.resources.log_out_icon
import chirpappkmp.core.designsystem.generated.resources.users_icon
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.logout
import chirpappkmp.feature.chat.presentation.generated.resources.profile_settings
import com.example.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.example.core.designsystem.components.dropdown.ChirpDropDownMenu
import com.example.core.designsystem.components.dropdown.DropDownItem
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import chirpappkmp.core.designsystem.generated.resources.Res as DesignSystemRes


@Composable
fun ProfileAvatarSection(
    localParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) = with(localParticipant) {
    Box(
        modifier = modifier
    ) {
        this@with?.let {
            ChirpAvatarPhoto(
                displayText = initials,
                imageUrl = imageUrl,
                onClick = onClick
            )
        }
        //use above component as anchor
        ChirpDropDownMenu(
            isOpen = isMenuOpen,
            onDismiss = onDismissMenu,
            items = listOf(
                DropDownItem(
                    title = stringResource(Res.string.profile_settings),
                    icon = vectorResource(DesignSystemRes.drawable.users_icon),
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = onProfileSettingsClick
                ),
                DropDownItem(
                    title = stringResource(Res.string.logout),
                    icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = onLogoutClick
                )
            )
        )
    }
}

@Preview
@Composable
fun PreviewProfileAvatarSection() {
    ChirpTheme {
        ProfileAvatarSection(
            localParticipant = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            ),
            isMenuOpen = false,
            onClick = {},
            onDismissMenu = {},
            onProfileSettingsClick = {},
            onLogoutClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewDarkProfileAvatarSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        ProfileAvatarSection(
            localParticipant = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            ),
            isMenuOpen = false,
            onClick = {},
            onDismissMenu = {},
            onProfileSettingsClick = {},
            onLogoutClick = {},
        )
    }
}

//There is some preview bug, use interactive mode
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewProfileAvatarSectionMenuOpen() {
    ChirpTheme {
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            ProfileAvatarSection(
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum",
                    initials = "LI"
                ),
                isMenuOpen = true,
                onClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {},
            )
        }
    }
}

//There is some preview bug, use interactive mode
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewDarkProfileAvatarSectionMenuOpen() {
    ChirpTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            ProfileAvatarSection(
                localParticipant = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "Lorem ipsum2",
                    initials = "LI2"
                ),
                isMenuOpen = true,
                onClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {},
            )
        }
    }
}