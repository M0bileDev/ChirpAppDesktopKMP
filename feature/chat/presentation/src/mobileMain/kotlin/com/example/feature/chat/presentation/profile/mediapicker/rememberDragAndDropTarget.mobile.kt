package com.example.feature.chat.presentation.profile.mediapicker

import androidx.compose.runtime.remember
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget

@androidx.compose.runtime.Composable
actual fun rememberDragAndDropTarget(
    onHover: (Boolean) -> Unit,
    onDrop: (ByteArray) -> Unit
): DragAndDropTarget {
    return remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                return false
            }
        }
    }
}