package com.example.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

val LightColorScheme = lightColorScheme(
    primary = ChirpBrand500,
    onPrimary = ChirpBrand1000,
    primaryContainer = ChirpBrand100,
    onPrimaryContainer = ChirpBrand900,

    secondary = ChirpBase700,
    onSecondary = ChirpBase0,
    secondaryContainer = ChirpBase100,
    onSecondaryContainer = ChirpBase900,

    tertiary = ChirpBrand900,
    onTertiary = ChirpBase0,
    tertiaryContainer = ChirpBrand100,
    onTertiaryContainer = ChirpBrand1000,

    error = ChirpRed500,
    onError = ChirpBase0,
    errorContainer = ChirpRed200,
    onErrorContainer = ChirpRed600,

    background = ChirpBrand1000,
    onBackground = ChirpBase0,
    surface = ChirpBase0,
    onSurface = ChirpBase1000,
    surfaceVariant = ChirpBase100,
    onSurfaceVariant = ChirpBase900,

    outline = ChirpBase1000Alpha8,
    outlineVariant = ChirpBase200,
)

val DarkColorScheme = darkColorScheme(
    primary = ChirpBrand500,
    onPrimary = ChirpBrand1000,
    primaryContainer = ChirpBrand900,
    onPrimaryContainer = ChirpBrand500,

    secondary = ChirpBase400,
    onSecondary = ChirpBase1000,
    secondaryContainer = ChirpBase900,
    onSecondaryContainer = ChirpBase150,

    tertiary = ChirpBrand500,
    onTertiary = ChirpBase1000,
    tertiaryContainer = ChirpBrand900,
    onTertiaryContainer = ChirpBrand500,

    error = ChirpRed500,
    onError = ChirpBase0,
    errorContainer = ChirpRed600,
    onErrorContainer = ChirpRed200,

    background = ChirpBase1000,
    onBackground = ChirpBase0,
    surface = ChirpBase950,
    onSurface = ChirpBase0,
    surfaceVariant = ChirpBase900,
    onSurfaceVariant = ChirpBase150,

    outline = ChirpBase100Alpha10,
    outlineVariant = ChirpBase800,
)