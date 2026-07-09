package com.example.chirpappkmp

import com.example.chirpappkmp.windows.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

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

    fun onAddWindowClick() {
        _state.update {
            it.copy(
                windows = it.windows + WindowState()
            )
        }
    }

    fun onRemoveWindowClick(id: String) {
        _state.update {
            it.copy(
                windows = it.windows.filter { window -> window.id != id }
            )
        }
    }
}