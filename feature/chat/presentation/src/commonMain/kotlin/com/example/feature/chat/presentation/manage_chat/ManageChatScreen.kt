package com.example.feature.chat.presentation.manage_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.chat_members
import chirpappkmp.feature.chat.presentation.generated.resources.save
import com.example.core.designsystem.components.dialogs.ChirpAdaptiveDialogSheetLayout
import com.example.core.presentation.util.ObserveAsEvents
import com.example.feature.chat.presentation.components.ManageChatScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageChatRoot(
    chatId: String?,
    onDismiss: () -> Unit,
    onMembersAdded: () -> Unit,
    viewModel: ManageChatViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(chatId) {
        viewModel.onAction(ManageChatAction.ChatParticipantsAction.OnSelectChat(chatId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ManageChatEvent.OnMembersAdded -> onMembersAdded()
        }
    }

    ChirpAdaptiveDialogSheetLayout(
        onDismiss = onDismiss,
    ) {
        ManageChatScreen(
            state = state,
            title = stringResource(Res.string.chat_members),
            primaryButtonText = stringResource(Res.string.save),
            onAction = { action ->
                when (action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}