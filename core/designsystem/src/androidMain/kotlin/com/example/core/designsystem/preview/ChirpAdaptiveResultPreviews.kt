package com.example.core.designsystem.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.core.designsystem.components.layouts.ChirpAdaptiveResultLayout
import com.example.core.designsystem.theme.ChirpTheme

@PreviewScreenSizes
@Composable
fun PreviewChirpAdaptiveResultLayout() {
    ChirpTheme {
        ChirpAdaptiveResultLayout(modifier = Modifier.fillMaxSize()) {
            Text("Lorem ipsum", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@PreviewScreenSizes
@Composable
fun PreviewDarkChirpAdaptiveResultLayout() {
    ChirpTheme(darkTheme = true) {
        ChirpAdaptiveResultLayout(modifier = Modifier.fillMaxSize()) {
            Text("Lorem ipsum", color = MaterialTheme.colorScheme.onSurface)
        }
    }
}