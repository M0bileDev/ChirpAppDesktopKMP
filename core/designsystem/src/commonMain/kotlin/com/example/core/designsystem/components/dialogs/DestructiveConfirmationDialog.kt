package com.example.core.designsystem.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import chirpappkmp.core.designsystem.generated.resources.Res
import chirpappkmp.core.designsystem.generated.resources.dismiss
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DestructiveConfirmationDialog(
    title: String,
    description: String,
    confirmationButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .padding(
                    horizontal = 24.dp,
                    vertical = 16.dp
                )
                .widthIn(max = 480.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(15.dp)
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChirpButton(
                        text = cancelButtonText,
                        onClick = onCancelClick,
                        style = ChirpButtonStyle.SECONDARY
                    )
                    ChirpButton(
                        text = confirmationButtonText,
                        onClick = onConfirmClick,
                        style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY
                    )
                }
            }
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onDismiss
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.dismiss),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDestructiveConfirmationDialog() {
    ChirpTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            DestructiveConfirmationDialog(
                title = "Lorem ipsum",
                description = "Lorem ipsum",
                confirmationButtonText = "Delete",
                cancelButtonText = "Cancel",
                onConfirmClick = {},
                onCancelClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview
@Composable
fun PreviewDarkDestructiveConfirmationDialog() {
    ChirpTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            DestructiveConfirmationDialog(
                title = "Lorem ipsum",
                description = "Lorem ipsum",
                confirmationButtonText = "Delete",
                cancelButtonText = "Cancel",
                onConfirmClick = {},
                onCancelClick = {},
                onDismiss = {}
            )
        }
    }
}