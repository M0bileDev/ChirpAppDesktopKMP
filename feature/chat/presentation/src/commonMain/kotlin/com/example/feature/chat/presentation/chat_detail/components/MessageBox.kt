package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.cloud_off_icon
import chirpappkmp.feature.chat.presentation.generated.resources.send
import chirpappkmp.feature.chat.presentation.generated.resources.send_a_message
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.textfields.ChirpMultiLineTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.feature.chat.domain.model.ConnectionState
import com.example.feature.chat.presentation.util.toUiText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageBox(
    messageTextFieldState: TextFieldState,
    isSendButtonEnabled: Boolean,
    connectionState: ConnectionState,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState == ConnectionState.CONNECTED
    val connectionStateText = connectionState.toUiText().asString()

    ChirpMultiLineTextField(
        modifier = modifier,
        state = messageTextFieldState,
        placeholder = stringResource(Res.string.send_a_message),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        onKeyboardAction = onSendClick,
        bottomContent = {
            Spacer(modifier = Modifier.weight(1f))
            if (!isConnected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = vectorResource(Res.drawable.cloud_off_icon),
                        contentDescription = connectionStateText,
                        tint = MaterialTheme.colorScheme.extended.textDisabled
                    )
                    Text(
                        text = connectionStateText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.extended.textDisabled
                    )
                }
            }
            ChirpButton(
                text = stringResource(Res.string.send),
                onClick = onSendClick,
                enabled = isConnected && isSendButtonEnabled
            )
        }
    )
}

@Preview
@Composable
fun PreviewMessageBox() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = true,
            connectionState = ConnectionState.CONNECTED,
            onSendClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageBox() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = true,
            connectionState = ConnectionState.CONNECTED,
            onSendClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewMessageBoxDisconnected() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = true,
            connectionState = ConnectionState.DISCONNECTED,
            onSendClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageBoxDisconnected() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = true,
            connectionState = ConnectionState.DISCONNECTED,
            onSendClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewMessageBoxInputDisabled() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = false,
            connectionState = ConnectionState.CONNECTING,
            onSendClick = {},
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageBoxInputDisabled() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageBox(
            messageTextFieldState = TextFieldState(),
            isSendButtonEnabled = false,
            connectionState = ConnectionState.CONNECTING,
            onSendClick = {},
        )
    }
}