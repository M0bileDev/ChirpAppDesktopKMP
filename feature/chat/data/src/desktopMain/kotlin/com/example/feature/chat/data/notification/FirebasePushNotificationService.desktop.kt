package com.example.feature.chat.data.notification

import com.example.feature.chat.domain.notification.PushNotificationTokenService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

actual class FirebasePushNotificationService :
    PushNotificationTokenService {
    actual override fun observeDeviceToken(): Flow<String?> {
        return emptyFlow()
    }
}