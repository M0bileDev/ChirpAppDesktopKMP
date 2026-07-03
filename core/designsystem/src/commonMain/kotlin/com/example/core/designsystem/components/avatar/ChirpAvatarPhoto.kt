package com.example.core.designsystem.components.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class AvatarSize(val dp: Dp) {
    REGULAR(40.dp),
    LARGE(60.dp)
}

@Composable
fun ChirpAvatarPhoto(
    displayText: String,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.REGULAR,
    imageUrl: String? = null,
    onClick: (() -> Unit)? = null,
    textColor: Color = MaterialTheme.colorScheme.extended.textPlaceholder
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .clickable(
                enabled = true,
                onClick = { onClick?.invoke() }
            )
            .background(MaterialTheme.colorScheme.extended.secondaryFill)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
        imageUrl?.let {
            AsyncImage(
                modifier = Modifier.clip(CircleShape).matchParentSize(),
                model = it,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewChirpAvatarPhoto() {
    ChirpTheme {
        ChirpAvatarPhoto(
            displayText = "LI"
        )
    }
}

@Preview
@Composable
fun PreviewChirpAvatarPhotoLarge() {
    ChirpTheme {
        ChirpAvatarPhoto(
            displayText = "LI",
            size = AvatarSize.LARGE
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpAvatarPhoto() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpAvatarPhoto(
            displayText = "LI"
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpAvatarPhotoLarge() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpAvatarPhoto(
            displayText = "LI",
            size = AvatarSize.LARGE
        )
    }
}