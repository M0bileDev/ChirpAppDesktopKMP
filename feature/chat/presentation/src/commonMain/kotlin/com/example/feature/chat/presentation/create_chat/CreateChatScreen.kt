@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.create_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.create_chat
import com.example.core.designsystem.components.dialogs.ChirpAdaptiveDialogSheetLayout
import com.example.core.presentation.util.ObserveAsEvents
import com.example.feature.chat.domain.model.Chat
import com.example.feature.chat.presentation.components.ManageChatScreen
import com.example.feature.chat.presentation.manage_chat.ManageChatAction
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun CreateChatRoot(
    onDismiss: () -> Unit,
    onChatCreated: (Chat) -> Unit,
    viewModel: CreateChatViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CreateChatEvent.OnChatCreated -> onChatCreated(event.chat)
        }
    }

    ChirpAdaptiveDialogSheetLayout(
        onDismiss = onDismiss,
    ) {
        val createChatTitle = stringResource(Res.string.create_chat)
        
        ManageChatScreen(
            state = state,
            title = createChatTitle,
            primaryButtonText = createChatTitle,
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


