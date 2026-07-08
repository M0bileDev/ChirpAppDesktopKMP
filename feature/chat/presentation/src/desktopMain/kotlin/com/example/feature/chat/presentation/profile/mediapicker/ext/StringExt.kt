package com.example.feature.chat.presentation.profile.mediapicker.ext

import com.example.feature.chat.presentation.profile.mediapicker.ImageExtension

fun String.getMimeTypeFromFileName(): String? {
    val extension = substringAfterLast(".", "").lowercase()
    val imageExtension = ImageExtension.entries.firstOrNull { it.name.lowercase() == extension }

    return imageExtension?.let { extension ->
        "image/${extension.name.lowercase()}"
    }
}

fun String.hasValidImageExtension(): Boolean {
    return ImageExtension.entries.any { extension -> endsWith(extension.name.lowercase()) }
}