package com.example.feature.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.add
import chirpappkmp.feature.chat.presentation.generated.resources.email_or_username
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.textfields.ChirpTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatParticipantSearchTextSection(
    queryState: TextFieldState,
    onAddClick: () -> Unit,
    isSearchEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChirpTextField(
            modifier = Modifier.weight(1f),
            state = queryState,
            placeholder = stringResource(Res.string.email_or_username),
            supportingText = error?.asString(),
            isError = error != null,
            singleLine = true,
            keyboardType = KeyboardType.Email,
            onFocusChanged = onFocusChanged
        )
        ChirpButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            style = ChirpButtonStyle.SECONDARY,
            enabled = isSearchEnabled,
            isLoading = isLoading
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewChatParticipantSearchTextSection() {
    ChirpTheme {
        ChatParticipantSearchTextSection(
            queryState = TextFieldState(),
            onAddClick = {},
            isSearchEnabled = true,
            isLoading = false,
            onFocusChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewChatParticipantSearchTextSectionError() {
    ChirpTheme {
        ChatParticipantSearchTextSection(
            queryState = TextFieldState(),
            onAddClick = {},
            isSearchEnabled = true,
            isLoading = false,
            error = UiText.DynamicString("Lorem ipsum"),
            onFocusChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkChatParticipantSearchTextSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatParticipantSearchTextSection(
            queryState = TextFieldState(),
            onAddClick = {},
            isSearchEnabled = true,
            isLoading = false,
            onFocusChanged = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkChatParticipantSearchTextSectionError() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatParticipantSearchTextSection(
            queryState = TextFieldState(),
            onAddClick = {},
            isSearchEnabled = true,
            isLoading = false,
            error = UiText.DynamicString("Lorem ipsum"),
            onFocusChanged = {}
        )
    }
}