package com.example.core.designsystem.components.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.core.presentation.composableUtil.currentDeviceConfiguration

@Composable
fun ChirpAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if (configuration.isMobile) {
        ChirpBottomSheet(
            modifier = modifier,
            onDismiss = onDismiss,
            content = content
        )
    } else {
        ChirpDialogContent(
            modifier = modifier,
            onDismiss = onDismiss,
            content = content
        )
    }
}