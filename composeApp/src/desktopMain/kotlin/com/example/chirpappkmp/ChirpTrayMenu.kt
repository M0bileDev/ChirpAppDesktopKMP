package com.example.chirpappkmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.TrayState
import chirpappkmp.composeapp.generated.resources.Res.drawable
import chirpappkmp.composeapp.generated.resources.Res.string
import chirpappkmp.composeapp.generated.resources.app_name
import chirpappkmp.composeapp.generated.resources.app_theme
import chirpappkmp.composeapp.generated.resources.logo
import chirpappkmp.core.designsystem.generated.resources.Res
import com.example.core.domain.preferences.ThemePreference
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ApplicationScope.ChirpTrayMenu(
    state: TrayState,
    themePreferenceFromAppSettings: ThemePreference,
    onThemePreferenceClick: (ThemePreference) -> Unit
) {
    Tray(
        icon = painterResource(drawable.logo),
        state = state
    ) {
        Menu(
            text = stringResource(string.app_theme)
        ) {
            ThemePreference.entries.forEach { themePreference ->
                CheckboxItem(
                    text = themePreference.name.lowercase().replaceFirstChar { it.titlecase() },
                    onCheckedChange = {
                        onThemePreferenceClick(themePreference)
                    },
                    checked = themePreferenceFromAppSettings == themePreference
                )
            }
        }
    }
}
