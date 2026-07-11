package com.example.chirpappkmp

import androidx.compose.ui.window.TrayState
import com.example.chirpappkmp.windows.WindowState
import com.example.core.domain.preferences.ThemePreference

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
    val themePreferences: ThemePreference = ThemePreference.SYSTEM,
    val trayState: TrayState = TrayState()
)