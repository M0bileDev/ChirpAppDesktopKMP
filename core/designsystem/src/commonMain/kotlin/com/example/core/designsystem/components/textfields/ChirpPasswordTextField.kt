package com.example.core.designsystem.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirpappkmp.core.designsystem.generated.resources.Res
import chirpappkmp.core.designsystem.generated.resources.hide_password
import chirpappkmp.core.designsystem.generated.resources.icon_eye_off
import chirpappkmp.core.designsystem.generated.resources.icon_eye_on
import chirpappkmp.core.designsystem.generated.resources.show_password
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpPasswordTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean = false,
    onToggleVisibilityClick: () -> Unit,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    ChirpTextFieldLayout(
        modifier = modifier,
        title = title,
        supportingText = supportingText,
        isError = isError,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
    ) { modifier, interactionSource ->
        BasicSecureTextField(
            state = state,
            modifier = modifier,
            enabled = enabled,
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(value = MaterialTheme.colorScheme.onSurface),
            decorator = { innerBox ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
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
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            vectorResource(Res.drawable.icon_eye_off)
                        } else {
                            vectorResource(Res.drawable.icon_eye_on)
                        },
                        contentDescription = if (isPasswordVisible) {
                            stringResource(Res.string.hide_password)
                        } else {
                            stringResource(Res.string.show_password)
                        },
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        modifier = Modifier.size(24.dp).clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                bounded = false,
                                radius = 24.dp
                            ),
                            onClick = onToggleVisibilityClick
                        )
                    )
                }
            }
        )
    }

}

@Composable
@Preview(showBackground = true)
private fun PreviewEmptyChirpPasswordTextField() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewFilledChirpPasswordTextField() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(
                initialText = "Lorem ipsum initial text"
            ),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDisabledChirpPasswordTextField() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            enabled = false
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewErrorChirpPasswordTextField() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            isError = true
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewEmptyDarkChirpPasswordTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewFilledDarkChirpPasswordTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpPasswordTextField(
            state = rememberTextFieldState(
                initialText = "Lorem ipsum initial text"
            ),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewDisabledDarkChirpPasswordTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            enabled = false
        )
    }
}

@Composable
@Preview(backgroundColor = 0xFF000000, showBackground = true)
private fun PreviewErrorDarkChirpPasswordTextField() {
    ChirpTheme(darkTheme = true) {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            placeholder = "Lorem ipsum placeholder",
            title = "Lorem ipsum title",
            supportingText = "Lorem ipsum supporting text",
            isError = true
        )
    }
}