package com.example.chirpappkmp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.data.util.PlatformUtils
import com.example.core.domain.auth.SessionStorage
import com.example.feature.chat.domain.notification.DeviceTokenService
import com.example.feature.chat.domain.notification.PushNotificationTokenService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage,
    private val pushNotificationService: PushNotificationTokenService,
    private val deviceTokenService: DeviceTokenService
) : ViewModel() {

    private var previousRefreshToken: String? = null
    private var previousDeviceToken: String? = null
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )
    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            _state.update {
                it.copy(
                    isCheckingAuth = false,
                    isLoggedIn = authInfo != null
                )
            }
        }
    }

    private fun observeSession() {
        sessionStorage
            .observeAuthInfo()
            .onEach { authInfo ->
                val currentRefreshToken = authInfo?.refreshToken
                //user has been logged in, current refresh token is not null, during application
                //lifecycle user made action that made token expired ->  currentRefreshToken = null
                val isSessionExpired = previousRefreshToken != null && currentRefreshToken == null

                if (isSessionExpired) {
                    sessionStorage.set(null)
                    _state.update {
                        it.copy(
                            isLoggedIn = false
                        )
                    }
                    previousDeviceToken?.let { deviceToken ->
                        deviceTokenService.unregisterToken(deviceToken)
                    }
                    eventChannel.send(MainEvent.OnSessionExpired)
                }

                previousRefreshToken = currentRefreshToken
            }
            .combine(pushNotificationService.observeDeviceToken()) { authInfo, deviceToken ->
                val canRegisterDeviceToken =
                    authInfo != null && deviceToken != previousDeviceToken && deviceToken != null
                if (canRegisterDeviceToken) {
                    registerTokenService(
                        token = deviceToken,
                        platform = PlatformUtils.getOSName()
                    )
                }
                previousDeviceToken = deviceToken
            }
            .launchIn(viewModelScope)
    }

    private fun registerTokenService(
        token: String,
        platform: String
    ) {
        viewModelScope.launch {
            deviceTokenService
                .registerToken(
                    token = token,
                    platform = platform
                )
        }
    }
}