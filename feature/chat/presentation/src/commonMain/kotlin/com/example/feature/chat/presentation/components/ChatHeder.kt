package com.example.feature.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 80.dp)
                .padding(
                    vertical = 20.dp,
                    horizontal = 16.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
        ChirpHorizontalDivider()
    }
}

@Preview
@Composable
fun PreviewChatHeader() {
    ChirpTheme {
        ChatHeader {
            Box(modifier = Modifier.background(Color.Blue).fillMaxSize())
        }
    }
}