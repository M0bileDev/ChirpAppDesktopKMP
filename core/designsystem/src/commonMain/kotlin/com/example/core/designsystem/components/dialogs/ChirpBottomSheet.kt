@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.core.designsystem.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            //expand with animation
            sheetState.expand()
        }
    }

    ModalBottomSheet(
        sheetState = sheetState,
        modifier = modifier.statusBarsPadding(),
        onDismissRequest = onDismiss,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(left = 0.dp) }
    ) {
        content()
    }
}

@Preview
@Composable
fun PreviewChirpBottomSheet() {
    ChirpTheme {
        ChirpBottomSheet(
            onDismiss = {},
            content = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Lorem ipsum", modifier = Modifier.align(Alignment.Center))
                }
            }
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpBottomSheet() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpBottomSheet(
            onDismiss = {},
            content = {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Lorem ipsum", modifier = Modifier.align(Alignment.Center))
                }
            }
        )
    }
}