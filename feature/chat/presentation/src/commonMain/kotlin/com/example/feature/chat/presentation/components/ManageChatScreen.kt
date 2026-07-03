@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.cancel
import chirpappkmp.feature.chat.presentation.generated.resources.create_chat
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.composableUtil.currentDeviceConfiguration
import com.example.core.presentation.util.DeviceConfiguration
import com.example.core.presentation.util.clearFocusOnTap
import com.example.feature.chat.presentation.manage_chat.ManageChatAction
import com.example.feature.chat.presentation.manage_chat.ManageChatState
import com.example.feature.chat.presentation.type_alias.ChatParticipantUi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ManageChatScreen(
    state: ManageChatState,
    title: String,
    primaryButtonText: String,
    onAction: (ManageChatAction) -> Unit
) = with(state) {

    var isTextFieldFocused by remember { mutableStateOf(false) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val configuration = currentDeviceConfiguration()

    val shouldHideHeader =
        configuration == DeviceConfiguration.MOBILE_LANDSCAPE && isKeyboardVisible && isTextFieldFocused

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.surface)
            .imePadding()
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !shouldHideHeader
        ) {
            Column {
                ManageChatHeaderRow(
                    modifier = Modifier.fillMaxWidth(),
                    title = title,
                    onCloseClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    }
                )
                ChirpHorizontalDivider()
            }
        }
        ChatParticipantSearchTextSection(
            modifier = Modifier.fillMaxWidth(),
            queryState = queryTextState,
            onAddClick = {
                onAction(ManageChatAction.OnAddClick)
            },
            isSearchEnabled = canAddParticipant,
            isLoading = isSearching,
            error = searchError,
            onFocusChanged = {
                isTextFieldFocused = it
            }
        )
        ChirpHorizontalDivider()
        ChatParticipantsSelectionSection(
            modifier = Modifier.fillMaxWidth(),
            existingChatParticipants = existingChatParticipants,
            selectedParticipants = selectedChatParticipants,
            searchResult = currentSearchResult
        )
        ChirpHorizontalDivider()
        ManageChatButtonSection(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 16.dp),
            primaryButton = {
                ChirpButton(
                    text = primaryButtonText,
                    onClick = {
                        onAction(ManageChatAction.OnPrimaryActionButtonClick)
                    },
                    enabled = selectedChatParticipants.isNotEmpty(),
                    isLoading = isSubmitting
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = stringResource(Res.string.cancel),
                    onClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    },
                    style = ChirpButtonStyle.SECONDARY
                )
            },
            errorMessage = submitError?.asString()
        )
    }
}

@Preview
@Composable
fun PreviewManageChatScreen() {
    ChirpTheme {
        ManageChatScreen(
            ManageChatState(
                selectedChatParticipants = listOf(
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
            ),
            title = stringResource(Res.string.create_chat),
            primaryButtonText = stringResource(Res.string.create_chat),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkManageChatScreen() {
    ChirpTheme(darkTheme = true) {
        ManageChatScreen(
            ManageChatState(
                selectedChatParticipants = listOf(
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
            ),
            title = stringResource(Res.string.create_chat),
            primaryButtonText = stringResource(Res.string.create_chat),
            onAction = {}
        )
    }
}