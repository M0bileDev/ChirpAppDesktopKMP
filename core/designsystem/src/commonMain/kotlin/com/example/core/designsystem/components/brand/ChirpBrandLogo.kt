package com.example.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirpappkmp.core.designsystem.generated.resources.Res
import chirpappkmp.core.designsystem.generated.resources.logo_chirp
import com.example.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpBrandLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        imageVector = vectorResource(Res.drawable.logo_chirp),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Preview
@Composable
fun PreviewChirpBrandLogo(){
    ChirpTheme {
        ChirpBrandLogo()
    }
}

@Preview
@Composable
fun PreviewDarkChirpBrandLogo(){
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpBrandLogo()
    }
}