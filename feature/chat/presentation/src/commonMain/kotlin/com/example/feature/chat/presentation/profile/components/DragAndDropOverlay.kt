package com.example.feature.chat.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.cloud_computing
import chirpappkmp.feature.chat.presentation.generated.resources.drop_a_profile_image
import chirpappkmp.feature.chat.presentation.generated.resources.upload_image
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DragAndDropOverlay(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(100.dp),
            imageVector = vectorResource(Res.drawable.cloud_computing),
            contentDescription = stringResource(Res.string.upload_image),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.drop_a_profile_image),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDragAndDropOverlay(){
    ChirpTheme {
        DragAndDropOverlay()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkDragAndDropOverlay(){
    ChirpTheme(
        darkTheme = true
    ) {
        DragAndDropOverlay()
    }
}