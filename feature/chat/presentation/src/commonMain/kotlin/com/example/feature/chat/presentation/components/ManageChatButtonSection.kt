package com.example.feature.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ManageChatButtonSection(
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
        ) {
            primaryButton()
            secondaryButton()
        }
        AnimatedVisibility(
            visible = errorMessage != null
        ) {
            errorMessage?.let {
                Text(
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                    text = errorMessage,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewManageChatButtonSection() {
    ChirpTheme {
        ManageChatButtonSection(
            modifier = Modifier.fillMaxWidth(),
            primaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            },
            secondaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            }
        )
    }
}

@Preview
@Composable
fun PreviewManageChatButtonSectionError() {
    ChirpTheme {
        ManageChatButtonSection(
            modifier = Modifier.fillMaxWidth(),
            errorMessage = "Lorem ipsum",
            primaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            },
            secondaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            }
        )
    }
}

@Preview
@Composable
fun PreviewDarkManageChatButtonSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        ManageChatButtonSection(
            modifier = Modifier.fillMaxWidth(),
            primaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            },
            secondaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            }
        )
    }
}

@Preview
@Composable
fun PreviewManageChatButtonSectionErrorDark() {
    ChirpTheme(
        darkTheme = true
    ) {
        ManageChatButtonSection(
            modifier = Modifier.fillMaxWidth(),
            errorMessage = "Lorem ipsum",
            primaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            },
            secondaryButton = {
                ChirpButton(text = "Lorem ipsum", onClick = {})
            }
        )
    }
}
