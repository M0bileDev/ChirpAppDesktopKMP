package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.check_icon
import chirpappkmp.feature.chat.presentation.generated.resources.failed
import chirpappkmp.feature.chat.presentation.generated.resources.loading_icon
import chirpappkmp.feature.chat.presentation.generated.resources.sending
import chirpappkmp.feature.chat.presentation.generated.resources.sent
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.designsystem.theme.labelXSmall
import com.example.feature.chat.domain.model.ChatMessageDeliveryStatus
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageStatus(
    status: ChatMessageDeliveryStatus,
    modifier: Modifier = Modifier
) {
    val (text, icon, color) = when (status) {
        ChatMessageDeliveryStatus.SENDING -> Triple(
            stringResource(Res.string.sending),
            vectorResource(Res.drawable.loading_icon),
            MaterialTheme.colorScheme.extended.textDisabled
        )

        ChatMessageDeliveryStatus.SENT -> Triple(
            stringResource(Res.string.sent),
            vectorResource(Res.drawable.check_icon),
            MaterialTheme.colorScheme.extended.textTertiary
        )

        ChatMessageDeliveryStatus.FAILED -> Triple(
            stringResource(Res.string.failed),
            Icons.Default.Close,
            MaterialTheme.colorScheme.error
        )
    }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = icon,
            contentDescription = text,
            tint = color
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelXSmall
        )
    }
}

@Preview
@Composable
fun PreviewMessageStatusSending() {
    ChirpTheme {
        MessageStatus(
            status = ChatMessageDeliveryStatus.SENDING
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageStatusSending() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageStatus(
            status = ChatMessageDeliveryStatus.SENDING
        )
    }
}

@Preview
@Composable
fun PreviewMessageStatusSent() {
    ChirpTheme {
        MessageStatus(
            status = ChatMessageDeliveryStatus.SENT
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageStatusSent() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageStatus(
            status = ChatMessageDeliveryStatus.SENT
        )
    }
}

@Preview
@Composable
fun PreviewMessageStatusFailed() {
    ChirpTheme {
        MessageStatus(
            status = ChatMessageDeliveryStatus.FAILED
        )
    }
}

@Preview
@Composable
fun PreviewDarkMessageStatusFailed() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageStatus(
            status = ChatMessageDeliveryStatus.FAILED
        )
    }
}