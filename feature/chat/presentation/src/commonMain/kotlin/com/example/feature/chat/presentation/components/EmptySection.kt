package com.example.feature.chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.empty_chat
import chirpappkmp.feature.chat.presentation.generated.resources.no_messages
import chirpappkmp.feature.chat.presentation.generated.resources.no_messages_subtitle
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.composableUtil.currentDeviceConfiguration
import com.example.core.presentation.util.DeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EmptySection(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val configuration = currentDeviceConfiguration()
    val size = if (configuration == DeviceConfiguration.MOBILE_LANDSCAPE) {
        125.dp
    } else {
        200.dp
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(size),
            painter = painterResource(Res.drawable.empty_chat),
            contentDescription = title,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.extended.textPrimary
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.extended.textSecondary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewEmptyChatSection() {
    ChirpTheme {
        EmptySection(
            modifier = Modifier.fillMaxSize(),
            title = stringResource(Res.string.no_messages),
            description = stringResource(Res.string.no_messages_subtitle)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkEmptyChatSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        EmptySection(
            modifier = Modifier.fillMaxSize(),
            title = stringResource(Res.string.no_messages),
            description = stringResource(Res.string.no_messages_subtitle)
        )
    }
}