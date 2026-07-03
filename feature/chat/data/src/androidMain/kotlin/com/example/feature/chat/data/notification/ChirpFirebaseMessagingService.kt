package com.example.feature.chat.data.notification

import com.example.core.domain.auth.SessionStorage
import com.example.feature.chat.domain.notification.DeviceTokenService
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ChirpFirebaseMessagingService : FirebaseMessagingService() {

    private val deviceTokenService by inject<DeviceTokenService>()
    private val sessionStorage by inject<SessionStorage>()
    private val applicationScope by inject<CoroutineScope>()

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        applicationScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            authInfo?.let {
                deviceTokenService.registerToken(
                    token = token,
                    platform = ANDROID_PLATFORM
                )
            }
        }

    }

    companion object {
        const val ANDROID_PLATFORM = "ANDROID"
    }
}