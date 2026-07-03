package com.example.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirpappkmp.core.designsystem.generated.resources.Res
import chirpappkmp.core.designsystem.generated.resources.success_checkmark
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSuccessIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewChirpSuccessIcon(){
    ChirpTheme {
        ChirpSuccessIcon()
    }
}

@Preview
@Composable
fun PreviewDarkChirpSuccessIcon(){
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpSuccessIcon()
    }
}