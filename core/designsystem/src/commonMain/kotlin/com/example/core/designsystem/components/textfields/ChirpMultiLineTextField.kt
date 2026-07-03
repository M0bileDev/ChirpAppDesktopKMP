package com.example.core.designsystem.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpMultiLineTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    maxHeightInLines: Int = 3,
    bottomContent: @Composable (RowScope.() -> Unit)? = null
) {
    val textFieldRequester = remember {
        FocusRequester()
    }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.extended.surfaceLower,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    textFieldRequester.requestFocus()
                }
            )
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = textFieldRequester),
            state = state,
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.extended.textPrimary
            ),
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = 1,
                maxHeightInLines = maxHeightInLines
            ),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = { onKeyboardAction() },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.extended.textPrimary),
            decorator = { innerBox ->
                if (placeholder != null && state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerBox()
            }
        )
        bottomContent?.let { content ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomContent(this)
            }
        }
    }
}

@Preview
@Composable
fun PreviewChirpMultiLineTextField() {
    ChirpTheme {
        ChirpMultiLineTextField(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .heightIn(max = 150.dp),
            state = rememberTextFieldState(
                initialText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            ),
            placeholder = null,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpMultiLineTextField() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpMultiLineTextField(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .heightIn(max = 150.dp),
            state = rememberTextFieldState(
                initialText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            ),
            placeholder = null,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))
                ChirpButton(
                    text = "Lorem ipsum",
                    onClick = {}
                )
            }
        )
    }
}