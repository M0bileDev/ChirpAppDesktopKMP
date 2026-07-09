package com.example.chirpappkmp

import com.example.chirpappkmp.windows.WindowState

data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState())
)