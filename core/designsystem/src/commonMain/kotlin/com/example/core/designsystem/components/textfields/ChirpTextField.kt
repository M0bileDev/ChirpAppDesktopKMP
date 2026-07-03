package com.example.core.designsystem.components.textfields

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    singleLine: Boolean = false,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    ChirpTextFieldLayout(
        modifier = modifier,
        title = title,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
    ) { styleModifier, interactionSource ->
        ChirpTextFieldBody(
            enabled,
            state,
            singleLine,
            keyboardType,
            interactionSource,
            placeholder,
            styleModifier
        )
    }
}

@Composable
private fun ChirpTextFieldBody(
    enabled: Boolean,
    state: TextFieldState,
    singleLine: Boolean,
    keyboardType: KeyboardType,
    interactionSource: MutableInteractionSource,
    placeholder: String?,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        lineLimits = if (singleLine) {
            TextFieldLineLimits.SingleLine
        } else {
            TextFieldLineLimits.Default
        },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = if (enabled) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.extended.textPlaceholder
            }
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onSurface),
        interactionSource = interactionSource,
        decorator = { innerBox ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (state.text.isEmpty() && placeholder != null) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                innerBox()
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewEmptyChirpTextField() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewFilledChirpTextField() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(
                initialText = "Lorem ipsum initial text"
            ),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDisabledChirpTextField() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            enabled = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewErrorChirpTextField() {
    ChirpTheme {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            isError = true
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewEmptyDarkChirpTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewFilledDarkChirpTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpTextField(
            state = rememberTextFieldState(
                initialText = "Lorem ipsum initial text"
            ),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewDisabledDarkChirpTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            enabled = false
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewErrorDarkChirpTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            isError = true
        )
    }
}