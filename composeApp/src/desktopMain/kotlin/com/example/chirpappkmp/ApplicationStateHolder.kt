package com.example.chirpappkmp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope
) {
    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart { }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            ApplicationState()
        )
}