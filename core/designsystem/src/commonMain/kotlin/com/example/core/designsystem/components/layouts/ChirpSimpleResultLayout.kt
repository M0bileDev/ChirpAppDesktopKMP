package com.example.core.designsystem.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.brand.ChirpSuccessIcon
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSimpleResultLayout(
    title: String,
    description: String,
    error: String? = null,
    icon: @Composable ColumnScope.() -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Column(
            modifier = Modifier.fillMaxWidth().offset(y = -(25.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            primaryButton()
            if (secondaryButton != null) {
                Spacer(modifier = Modifier.height(8.dp))
                secondaryButton()
            }
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChirpSimpleResultLayout() {
    ChirpTheme {
        ChirpSimpleResultLayout(
            title = "Lorem ipsum",
            description = "Lorem ipsum",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {}
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChirpSimpleResultLayoutError() {
    ChirpTheme {
        ChirpSimpleResultLayout(
            title = "Lorem ipsum",
            description = "Lorem ipsum",
            error = "Lorem ipsum",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {}
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkChirpSimpleResultLayout() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpSimpleResultLayout(
            title = "Lorem ipsum",
            description = "Lorem ipsum",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {}
                )
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkChirpSimpleResultLayoutError() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpSimpleResultLayout(
            title = "Lorem ipsum",
            description = "Lorem ipsum",
            error = "Lorem ipsum",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Lorem ipsum",
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {}
                )
            }
        )
    }
}