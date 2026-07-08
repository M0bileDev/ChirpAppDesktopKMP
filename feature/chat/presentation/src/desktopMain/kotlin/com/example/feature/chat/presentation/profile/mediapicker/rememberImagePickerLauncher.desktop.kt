package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.select_a_profile_image
import com.example.feature.chat.presentation.profile.mediapicker.ext.hasValidImageExtension
import com.example.feature.chat.presentation.profile.mediapicker.ext.suspendToPickedImage
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
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
                    filenameFilter = FilenameFilter { _, name -> name.hasValidImageExtension() }
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