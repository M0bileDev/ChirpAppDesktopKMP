package com.example.core.designsystem.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.example.core.designsystem.components.brand.ChirpBrandLogo
import com.example.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import com.example.core.designsystem.theme.ChirpTheme

@Composable
@PreviewScreenSizes
fun PreviewChirpAdaptiveFormLayout() {
    ChirpTheme {
        ChirpAdaptiveFormLayout(
            headerText = "Lorem ipsum",
            errorText = "Lorem ipsum",
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                Text(text = "Lorem ipsum", color = MaterialTheme.colorScheme.onSurface)
            }
        )
    }
}

@Composable
@PreviewScreenSizes
fun PreviewDarkChirpAdaptiveFormLayout() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpAdaptiveFormLayout(
            headerText = "Lorem ipsum",
            errorText = "Lorem ipsum",
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                Text(text = "Lorem ipsum", color = MaterialTheme.colorScheme.onSurface)
            }
        )
    }
}