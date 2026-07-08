package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.select_a_profile_image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

@androidx.compose.runtime.Composable
actual fun rememberImagePicker(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val dialogTitle = stringResource(Res.string.select_a_profile_image)

    return remember {
        ImagePickerLauncher(
            onLaunch = {
                scope.launch {
                    pickImage(fileDialogTitle = dialogTitle)?.let { data ->
                        onResult(data)
                    }
                }
            }
        )
    }
}

private enum class ImageExtension {
    PNG,
    JPEG,
    JPG,
    WEBP
}

private suspend fun pickImage(fileDialogTitle: String): PickedImageData? {
    val file = suspendCancellableCoroutine { continuation ->
        var fileDialog: FileDialog? = null

        continuation.invokeOnCancellation {
            // emit global event
            SwingUtilities.invokeLater {
                fileDialog?.dispose()
            }
        }

        SwingUtilities.invokeLater {
            try {
                fileDialog = FileDialog(Frame(), fileDialogTitle, FileDialog.LOAD)
                fileDialog.apply {
                    filenameFilter = FilenameFilter { _, name ->
                        ImageExtension.entries.any { extension -> name.endsWith(extension.name.lowercase()) }
                    }
                    isVisible = true
                }
                val file = File(fileDialog.directory, fileDialog.file)
                continuation.resume(file)
            } catch (_: Exception) {
                continuation.resume(null)
            }
        }
    }

    return file?.suspendToPickedImage()
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
        "image/${extension.name.lowercase()}"
    }
}