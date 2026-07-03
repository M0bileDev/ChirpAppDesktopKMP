package com.example.feature.chat.domain.notification

import kotlinx.coroutines.flow.Flow

interface PushNotificationTokenService {
    fun observeDeviceToken(): Flow<String?>
}