@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)

package com.example.feature.chat.presentation.chat_list_detail

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.util.DialogSheetScopedViewModel
import com.example.feature.chat.presentation.chat_detail.ChatDetailRoot
import com.example.feature.chat.presentation.chat_list.ChatListRoot
import com.example.feature.chat.presentation.create_chat.CreateChatRoot
import com.example.feature.chat.presentation.manage_chat.ManageChatRoot
import com.example.feature.chat.presentation.profile.ProfileRoot
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListDetailAdaptiveLayout(
    initialChatId: String?,
    onLogout: () -> Unit,
    viewModel: ChatListDetailViewModel = koinViewModel<ChatListDetailViewModel>()
) {
    val sharedState by viewModel.state.collectAsStateWithLifecycle()
    val paneScaffoldDirective = createPaneScaffoldDirectiveByConfigurationAndAdaptiveInfo()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = paneScaffoldDirective
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(initialChatId) {
        initialChatId?.let { chatId ->
            viewModel.onAction(ChatListDetailAction.OnSelectChat(chatId = chatId))
            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    BackHandler(
        enabled = scaffoldNavigator.canNavigateBack()
    ) {
        scope.launch {
            scaffoldNavigator.navigateBack()
            viewModel.onAction(ChatListDetailAction.OnSelectChat(null))
        }
    }

    val detailPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail]
    LaunchedEffect(detailPane, sharedState.selectedChatId) {
        if (detailPane == PaneAdaptedValue.Hidden && sharedState.selectedChatId != null) {
            viewModel.onAction(ChatListDetailAction.OnSelectChat(null))
        }
    }

    ListDetailPaneScaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        directive = paneScaffoldDirective,
        value = scaffoldNavigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                ChatListRoot(
                    selectedChatId = sharedState.selectedChatId,
                    onSelectChat = { chatId ->
                        viewModel.onAction(ChatListDetailAction.OnSelectChat(chatId))
                        scope.launch {
                            scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                        }
                    },
                    onSuccessfulLogout = onLogout,
                    onCreateChatClick = {
                        viewModel.onAction(ChatListDetailAction.OnCreateChatClick)
                    },
                    onProfileSettingsClick = {
                        viewModel.onAction(ChatListDetailAction.OnProfileSettingsClick)
                    },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                val listPane = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List]
                ChatDetailRoot(
                    chatId = sharedState.selectedChatId,
                    isDetailPresent = detailPane == PaneAdaptedValue.Expanded && listPane == PaneAdaptedValue.Expanded,
                    onChatMembersClick = {
                        viewModel.onAction(ChatListDetailAction.OnManageChatClick)
                    },
                    onBack = {
                        scope.launch {
                            if (scaffoldNavigator.canNavigateBack()) {
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    }
                )
            }
        },
    )

    DialogSheetScopedViewModel(
        visible = sharedState.dialogState is DialogState.CreateChat
    ) {
        CreateChatRoot(
            onDismiss = {
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog)
            },
            onChatCreated = { chat ->
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog)
                viewModel.onAction(ChatListDetailAction.OnSelectChat(chat.id))
                scope.launch {
                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }
        )
    }

    DialogSheetScopedViewModel(
        visible = sharedState.dialogState is DialogState.ManageChat
    ) {
        ManageChatRoot(
            chatId = sharedState.selectedChatId,
            onDismiss = {
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog)
            },
            onMembersAdded = {
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog)
            }
        )
    }

    DialogSheetScopedViewModel(
        visible = sharedState.dialogState is DialogState.Profile
    ) {
        ProfileRoot(
            onDismiss = {
                viewModel.onAction(ChatListDetailAction.OnDismissCurrentDialog)
            }
        )
    }
}