package com.example.core.designsystem.components.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpDialogContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(540.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.surface
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun PreviewChirpDialogContent() {
    ChirpTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ChirpDialogContent(
                onDismiss = {},
                content = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Lorem ipsum", modifier = Modifier.align(Alignment.Center))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewDarkChirpDialogContent() {
    ChirpTheme(darkTheme = true) {
        Box(modifier = Modifier.fillMaxSize()) {
            ChirpDialogContent(
                onDismiss = {},
                content = {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Lorem ipsum", modifier = Modifier.align(Alignment.Center))
                    }
                }
            )
        }
    }
}