package com.example.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    OutlinedIconButton(
        modifier = modifier.size(45.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary
        ),
        content = content
    )
}

@Preview
@Composable
private fun PreviewChirpButton() {
    ChirpTheme {
        ChirpIconButton(
            onClick = {},
            content = {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        )
    }
}

@Preview
@Composable
private fun PreviewDarkChirpButton() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpIconButton(
            onClick = {},
            content = {
                Icon(imageVector = Icons.Default.Check, contentDescription = "")
            }
        )
    }
}