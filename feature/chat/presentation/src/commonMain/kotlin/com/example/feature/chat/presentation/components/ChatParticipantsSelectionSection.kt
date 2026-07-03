@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.designsystem.theme.titleXSmall
import com.example.core.presentation.composableUtil.currentDeviceConfiguration
import com.example.core.presentation.util.DeviceConfiguration
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ColumnScope.ChatParticipantsSelectionSection(
    existingChatParticipants: List<ChatParticipantUi>,
    selectedParticipants: List<ChatParticipantUi>,
    modifier: Modifier = Modifier,
    searchResult: ChatParticipantUi? = null
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val rootHeightModifier = when (deviceConfiguration) {
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Modifier
                .animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }

        else -> Modifier.weight(1f)
    }

    Box(modifier = rootHeightModifier.then(modifier)) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                items = existingChatParticipants,
                key = { "existing_${it.id}" }
            ) { participant ->
                ChatParticipantListItem(
                    modifier = Modifier.fillMaxWidth(),
                    participantUi = participant
                )
            }
            if (existingChatParticipants.isNotEmpty()) {
                item {
                    ChirpHorizontalDivider()
                }
            }
            searchResult?.let {
                item {
                    ChatParticipantListItem(
                        modifier = Modifier.fillMaxWidth(),
                        participantUi = it
                    )
                }
            }
            item {
                ChirpHorizontalDivider()
            }
            if (selectedParticipants.isNotEmpty() && searchResult == null) {
                items(
                    items = selectedParticipants,
                    key = { it.id }
                ) { participant ->
                    ChatParticipantListItem(
                        modifier = Modifier.fillMaxWidth(),
                        participantUi = participant
                    )
                }
            }
        }
    }
}

@Composable
fun ChatParticipantListItem(
    participantUi: ChatParticipantUi,
    modifier: Modifier = Modifier
) = with(participantUi) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ChirpAvatarPhoto(
            displayText = initials,
            imageUrl = imageUrl
        )
        Text(
            text = username,
            style = MaterialTheme.typography.titleXSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewChatParticipantListItem() {
    ChirpTheme {
        ChatParticipantListItem(
            participantUi = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            )
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatParticipantListItem() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatParticipantListItem(
            participantUi = ChatParticipantUi(
                id = Uuid.random().toString(),
                username = "Lorem ipsum",
                initials = "LI"
            )
        )
    }
}

@Preview
@Composable
fun PreviewChatParticipantsSelectionSection() {
    ChirpTheme {
        Column {
            ChatParticipantsSelectionSection(
                existingChatParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    )
                ),
                selectedParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    )
                )
            )
        }
    }
}

@Preview
@Composable
fun PreviewDarkChatParticipantsSelectionSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        Column {
            ChatParticipantsSelectionSection(
                existingChatParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    )
                ),
                selectedParticipants = listOf(
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum",
                        initials = "LI"
                    ),
                    ChatParticipantUi(
                        id = Uuid.random().toString(),
                        username = "Lorem ipsum 2",
                        initials = "LI2"
                    )
                )
            )
        }
    }
}
