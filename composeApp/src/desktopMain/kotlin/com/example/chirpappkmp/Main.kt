package com.example.chirpappkmp

import androidx.compose.ui.window.application
import com.example.chirpappkmp.di.desktopModule
import com.example.chirpappkmp.di.initKoin
import com.example.chirpappkmp.windows.ChirpWindow

fun main() {
    initKoin {
        modules(
            desktopModule
        )
    }
    application {
        ChirpWindow(
            onCloseRequest = ::exitApplication,
            onAddWindowClick = {},
            onFocusChanged = {}
        )
    }
}