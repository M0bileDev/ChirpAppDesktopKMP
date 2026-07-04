package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.remember

@androidx.compose.runtime.Composable
actual fun rememberImagePicker(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    return remember {
        ImagePickerLauncher(
            onLaunch = {}
        )
    }
}