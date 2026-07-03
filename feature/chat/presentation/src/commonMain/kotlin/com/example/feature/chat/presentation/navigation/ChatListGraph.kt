package com.example.feature.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.feature.chat.presentation.chat_list_detail.ChatListDetailAdaptiveLayout

fun NavGraphBuilder.chatGraph(
    navController: NavController,
    onLogout: () -> Unit,
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetailRoute()
    ) {
        composable<ChatGraphRoutes.ChatListDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "chirp://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<ChatGraphRoutes.ChatListDetailRoute>()
            ChatListDetailAdaptiveLayout(
                initialChatId = args.chatId,
                onLogout = onLogout
            )
        }
    }
}