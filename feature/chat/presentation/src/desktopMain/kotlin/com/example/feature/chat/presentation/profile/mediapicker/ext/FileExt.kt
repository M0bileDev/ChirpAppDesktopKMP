package com.example.feature.chat.presentation.profile.mediapicker.ext

import com.example.feature.chat.presentation.profile.mediapicker.PickedImageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files

suspend fun File.suspendToPickedImage() = withContext(Dispatchers.IO) {
    return@withContext try {
        toPickedImageData()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        null
    }
}

fun File.toPickedImageData(): PickedImageData {
    val mimeType = name.getMimeTypeFromFileName()
    val bytes = Files.readAllBytes(toPath())

    return PickedImageData(
        bytes = bytes,
        mimeType = mimeType
    )
}