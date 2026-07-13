package com.example.chirpappkmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

@Composable
fun DeepLinkListener(
    navController: NavController,
    onSetup: () -> Unit,
) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(NavUri(uri))
        }

        onSetup()

        onDispose {
            ExternalUriHandler.listener = null
        }
    }
}