package com.example.feature.chat.presentation.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.close
import chirpappkmp.feature.chat.presentation.generated.resources.profile_settings
import com.example.core.designsystem.components.buttons.ChirpIconButton
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileHeaderSection(
    username: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Text(
                text = stringResource(Res.string.profile_settings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
        }
        ChirpIconButton(
            onClick = onCloseClick,
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.close),
                tint = MaterialTheme.colorScheme.extended.textPrimary
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewProfileHeaderSection() {
    ChirpTheme {
        ProfileHeaderSection(
            username = "Lorem ipsum",
            onCloseClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkProfileHeaderSection() {
    ChirpTheme(
        darkTheme = true
    ) {
        ProfileHeaderSection(
            username = "Lorem ipsum",
            onCloseClick = {}
        )
    }
}