package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.remember
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

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
    WEBP
}


private suspend fun File.suspendToPickedImage() = withContext(Dispatchers.IO) {
    return@withContext try {
        toPickedImageData()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        null
    }
}

private fun File.toPickedImageData(): PickedImageData {
    val mimeType = getMimeTypeFromFileName(filename = name)
    val bytes = Files.readAllBytes(toPath())

    return PickedImageData(
        bytes = bytes,
        mimeType = mimeType
    )
}

private fun getMimeTypeFromFileName(filename: String): String? {
    val extension = filename.substringAfterLast(".", "").lowercase()
    val imageExtension = ImageExtension.entries.firstOrNull { it.name.lowercase() == extension }

    return imageExtension?.let { extension ->
        "image/$extension"
    }
}