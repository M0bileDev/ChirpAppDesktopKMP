package com.example.feature.chat.data.notification

import com.example.feature.chat.domain.notification.PushNotificationTokenService
import kotlinx.coroutines.flow.Flow

expect class FirebasePushNotificationService : PushNotificationTokenService{
    override fun observeDeviceToken(): Flow<String?>
}