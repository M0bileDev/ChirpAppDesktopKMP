package com.example.chirpappkmp

import com.example.chirpappkmp.windows.WindowState
import com.example.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope,
    private val themePreferences: ThemePreferences
) {
    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            observeThemePreference()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            ApplicationState()
        )

    fun observeThemePreference() {
        themePreferences
            .observeThemePreference()
            .onEach { themePreference ->
                _state.update {
                    it.copy(
                        themePreferences = themePreference
                    )
                }
            }
            .launchIn(
                applicationScope
            )
    }

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