package com.example.chirpappkmp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.window.application
import com.example.chirpappkmp.di.desktopModule
import com.example.chirpappkmp.di.initKoin
import com.example.chirpappkmp.windows.ChirpWindow
import org.koin.compose.koinInject

fun main() {
    initKoin {
        modules(
            desktopModule
        )
    }
    application {
        val applicationStateHolder = koinInject<ApplicationStateHolder>()
        val applicationState by applicationStateHolder.state.collectAsState()
        val windows = applicationState.windows

        for (window in windows) {
            key(window.id) {
                ChirpWindow(
                    onCloseRequest = {
                        applicationStateHolder.onRemoveWindowClick(window.id)
                    },
                    onAddWindowClick = applicationStateHolder::onAddWindowClick,
                    onFocusChanged = {}
                )
            }
        }
    }
}