package com.example.core.designsystem.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpStackedAvatars(
    avatars: List<AvatarUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.REGULAR,
    visibleAvatarsCount: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    //a negative value means overlapping on the left side
    val overlapOffset = -(size.dp * overlapPercentage)
    val visibleAvatars = avatars.take(visibleAvatarsCount)
    val remainingCount = (avatars.size - visibleAvatarsCount).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarUi ->
            ChirpAvatarPhoto(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl
            )
        }

        if (remainingCount > 0) {
            ChirpAvatarPhoto(
                displayText = "$remainingCount+",
                size = size,
                textColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
fun PreviewChirpStackedAvatars() {
    ChirpTheme {
        ChirpStackedAvatars(
            avatars = listOf(
                AvatarUi("0", username = "Lorem ipsum", initials = "LI"),
                AvatarUi("1", username = "Lorem ipsum 1", initials = "LI1"),
                AvatarUi("2", username = "Lorem ipsum 2", initials = "LI2"),
            )
        )
    }
}

@Preview
@Composable
fun PreviewChirpStackedAvatarsTwoAvatars() {
    ChirpTheme {
        ChirpStackedAvatars(
            avatars = listOf(
                AvatarUi("0", username = "Lorem ipsum", initials = "LI"),
                AvatarUi("1", username = "Lorem ipsum 1", initials = "LI1"),
            )
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpStackedAvatars() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpStackedAvatars(
            avatars = listOf(
                AvatarUi("0", username = "Lorem ipsum", initials = "LI"),
                AvatarUi("1", username = "Lorem ipsum 1", initials = "LI1"),
                AvatarUi("2", username = "Lorem ipsum 2", initials = "LI2"),
            )
        )
    }
}

@Preview
@Composable
fun PreviewDarkChirpStackedAvatarsTwoAvatars() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpStackedAvatars(
            avatars = listOf(
                AvatarUi("0", username = "Lorem ipsum", initials = "LI"),
                AvatarUi("1", username = "Lorem ipsum 1", initials = "LI1"),
            )
        )
    }
}