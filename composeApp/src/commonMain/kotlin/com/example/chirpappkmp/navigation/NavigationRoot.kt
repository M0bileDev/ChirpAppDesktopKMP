package com.example.chirpappkmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.feature.auth.presentation.navigation.AuthGraphRoutes
import com.example.feature.auth.presentation.navigation.authGraph
import com.example.feature.chat.presentation.navigation.ChatGraphRoutes
import com.example.feature.chat.presentation.navigation.chatGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(ChatGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
        chatGraph(
            navController = navController,
            onLogout = {
                navController.navigate(AuthGraphRoutes.Graph) {
                    // pop up all routes to ChatGraphRoutes graphs, event this graph
                    popUpTo(ChatGraphRoutes.Graph){
                        inclusive = true
                    }
                }
            }
        )
    }
}