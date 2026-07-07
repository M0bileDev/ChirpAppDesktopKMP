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

enum class ImageExtension {
    PNG,
    JPEG,
    JPG,
    WEBP;

    fun getMimeTypeFromFileName(filename: String): String? {
        val extension = filename.substringAfterLast(".", "").lowercase()
        val imageExtension = ImageExtension.entries.firstOrNull { it.name.lowercase() == extension }

        return imageExtension?.let { extension ->
            "image/$extension"
        }
    }
}