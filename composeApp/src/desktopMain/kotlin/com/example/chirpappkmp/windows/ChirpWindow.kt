package com.example.chirpappkmp.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import chirpappkmp.composeapp.generated.resources.Res
import chirpappkmp.composeapp.generated.resources.app_name
import chirpappkmp.composeapp.generated.resources.logo
import com.example.chirpappkmp.App
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChirpWindow(
    onCloseRequest: () -> Unit,
    onAddWindowClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )

    Window(
        onCloseRequest = onCloseRequest,
        state = windowState,
        title = stringResource(Res.string.app_name),
        icon = painterResource(Res.drawable.logo)
    ) {
        App()
    }
}