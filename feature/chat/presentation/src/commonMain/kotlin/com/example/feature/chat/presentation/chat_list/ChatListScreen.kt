@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.chat_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.cancel
import chirpappkmp.feature.chat.presentation.generated.resources.create_chat
import chirpappkmp.feature.chat.presentation.generated.resources.logout
import chirpappkmp.feature.chat.presentation.generated.resources.logout_dialog_description
import chirpappkmp.feature.chat.presentation.generated.resources.logout_dialog_title
import chirpappkmp.feature.chat.presentation.generated.resources.no_chats
import chirpappkmp.feature.chat.presentation.generated.resources.no_chats_subtitle
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.components.buttons.ChirpFloatingActionButton
import com.example.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.permissions.Permission
import com.example.core.presentation.permissions.rememberPermissionController
import com.example.core.presentation.util.ObserveAsEvents
import com.example.feature.chat.presentation.chat_list.components.ChatListHeader
import com.example.feature.chat.presentation.chat_list.components.ChatListItem
import com.example.feature.chat.presentation.components.EmptySection
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ChatListRoot(
    selectedChatId: String?,
    onSelectChat: (String?) -> Unit,
    onSuccessfulLogout: () -> Unit,
    onCreateChatClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    viewModel: ChatListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedChatId) {
        viewModel.onAction(ChatListAction.OnSelectChat(selectedChatId))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ChatListEvent.OnLogoutError -> {
                scope.launch {
                    snackbarState.showSnackbar(
                        message = event.error.asStringAsync()
                    )
                }
            }

            ChatListEvent.OnLogoutSuccess -> onSuccessfulLogout()
        }
    }

    ChatListScreen(
        state = state,
        snackbarState = snackbarState,
        onAction = { action ->
            when (action) {
                is ChatListAction.OnSelectChat -> onSelectChat(action.chatId)
                ChatListAction.OnCreateChatClick -> onCreateChatClick()
                ChatListAction.OnProfileSettingsClick -> onProfileSettingsClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ChatListScreen(
    state: ChatListState,
    snackbarState: SnackbarHostState,
    onAction: (ChatListAction) -> Unit
) = with(state) {

    val permissionController = rememberPermissionController()
    LaunchedEffect(Unit) {
        permissionController.requestPermission(permission = Permission.NOTIFICATIONS)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(snackbarState)
        },
        floatingActionButton = {
            ChirpFloatingActionButton(
                onClick = {
                    onAction(ChatListAction.OnCreateChatClick)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(Res.string.create_chat)
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ChatListHeader(
                localParticipant = localParticipant,
                isUserMenuOpen = isUserManuOpen,
                onUserAvatarClick = {
                    onAction(ChatListAction.OnUserAvatarClick)
                },
                onDismissMenu = {
                    onAction(ChatListAction.OnDismissUserMenu)
                },
                onProfileSettingsClick = {
                    onAction(ChatListAction.OnProfileSettingsClick)
                },
                onLogoutClick = {
                    onAction(ChatListAction.OnLogoutClick)
                },
            )
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                chats.isEmpty() && !isLoading -> {
                    EmptySection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(
                                horizontal = 8.dp
                            ),
                        title = stringResource(Res.string.no_chats),
                        description = stringResource(Res.string.no_chats_subtitle)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(
                            items = chats,
                            key = { it.id }
                        ) { chatUi ->
                            ChatListItem(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onAction(ChatListAction.OnSelectChat(chatId = chatUi.id))
                                    },
                                chatUi = chatUi,
                                isSelected = chatUi.id == selectedChatId
                            )
                            ChirpHorizontalDivider()
                        }
                    }
                }
            }
        }
    }
    if (showLogoutConfirmation) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.logout_dialog_title),
            description = stringResource(Res.string.logout_dialog_description),
            confirmationButtonText = stringResource(Res.string.logout),
            cancelButtonText = stringResource(Res.string.cancel),
            onDismiss = {
                onAction(ChatListAction.OnDismissLogoutDialog)
            },
            onCancelClick = {
                onAction(ChatListAction.OnDismissLogoutDialog)
            },
            onConfirmClick = {
                onAction(ChatListAction.OnConfirmLogout)
            }
        )
    }
}

@Preview
@Composable
fun PreviewChatListScreen() {
    ChirpTheme {
        ChatListScreen(
            state = ChatListState(),
            snackbarState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkChatListScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatListScreen(
            state = ChatListState(),
            snackbarState = SnackbarHostState(),
            onAction = {}
        )
    }
}
