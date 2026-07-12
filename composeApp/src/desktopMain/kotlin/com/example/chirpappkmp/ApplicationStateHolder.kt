package com.example.chirpappkmp

import androidx.compose.ui.window.Notification
import com.example.chirpappkmp.windows.WindowState
import com.example.core.domain.preferences.ThemePreference
import com.example.core.domain.preferences.ThemePreferences
import com.example.feature.chat.data.notification.DesktopNotifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope,
    private val themePreferences: ThemePreferences,
    private val desktopNotifier: DesktopNotifier
) {
    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            observeThemePreference()
            observeNewMessages()
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

    fun observeNewMessages() {
        desktopNotifier
            .observeNewNotifications()
            .onEach { desktopNotificationPayload ->
                state.value.trayState
                    .sendNotification(
                        notification = Notification(
                            title = desktopNotificationPayload.title,
                            message = desktopNotificationPayload.message,
                            type = Notification.Type.Info
                        )
                    )
            }
            .launchIn(applicationScope)
    }

    fun onThemePreferenceClick(themePreference: ThemePreference) {
        applicationScope.launch {
            themePreferences.updateThemePreference(theme = themePreference)
        }
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

    fun onFocusChanged(id: String, isFocused: Boolean) {
        _state.update {
            it.copy(
                windows = it.windows.map { currentWindow ->
                    if (currentWindow.id == id) {
                        currentWindow.copy(isFocused = isFocused)
                    } else currentWindow
                }
            )
        }
    }
}