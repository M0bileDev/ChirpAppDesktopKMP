package com.example.chirpappkmp

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import com.example.chirpappkmp.deeplink.DesktopDeeplinkHandler
import com.example.chirpappkmp.di.desktopModule
import com.example.chirpappkmp.di.initKoin
import com.example.chirpappkmp.navigation.ExternalUriHandler
import com.example.chirpappkmp.theme.rememberAppTheme
import com.example.chirpappkmp.windows.ChirpWindow
import org.koin.compose.koinInject

fun main(args: Array<String>) {
    initKoin {
        modules(
            desktopModule
        )
    }

    DesktopDeeplinkHandler.setup()

    //App has been opened from deeplink
    val initialDeepLink = args.firstOrNull {
        val cleanDeepLink = it.trim('"')

        DesktopDeeplinkHandler.supportedUriPatterns.any { patter ->
            patter.matches(cleanDeepLink)
        }
    }?.trim('"')

    application {
        var canReceiveDeepLink by remember { mutableStateOf(false) }
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        //App has been opened from deeplink
        LaunchedEffect(canReceiveDeepLink) {
            if (canReceiveDeepLink && initialDeepLink != null) {
                ExternalUriHandler.onNewUri(initialDeepLink)
            }
        }

        LaunchedEffect(windows) {
            if (windows.isEmpty()) {
                exitApplication()
            }
        }

        val appTheme = rememberAppTheme(applicationState.themePreferences)

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    appTheme = appTheme,
                    onCloseRequest = {
                        applicationStateHolder.onRemoveWindowClick(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = { isFocused ->
                        applicationStateHolder.onFocusChanged(
                            id = window.id,
                            isFocused = isFocused
                        )
                    },
                    onDeepLinkListenerSetup = {
                        canReceiveDeepLink = true
                    }
                )
            }
        }

        ChirpTrayMenu(
            state = applicationState.trayState,
            themePreferenceFromAppSettings = applicationState.themePreferences,
            onThemePreferenceClick = applicationStateHolder::onThemePreferenceClick
        )
    }
}