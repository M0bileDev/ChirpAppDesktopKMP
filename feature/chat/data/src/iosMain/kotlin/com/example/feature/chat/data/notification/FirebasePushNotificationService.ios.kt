package com.example.feature.chat.data.notification

import com.example.core.domain.logging.ChirpLogger
import com.example.feature.chat.domain.notification.PushNotificationTokenService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications

actual class FirebasePushNotificationService(
    private val logger: ChirpLogger
) : PushNotificationTokenService {
    actual override fun observeDeviceToken(): Flow<String?> {
        return IosDeviceTokenHolder
            // fetch token
            .token
            // check on each emission
            .onStart {
                // if token is not available
                if (IosDeviceTokenHolder.token.value == null) {
                    // try to get token from shared preferences (ios user defaults)
                    val userDefaults = NSUserDefaults.standardUserDefaults
                    // get token from plist
                    val fcmToken = userDefaults.stringForKey(FCM_TOKEN_KEY)

                    if (fcmToken != null) {
                        // new emission of token state
                        IosDeviceTokenHolder.updateToken(fcmToken)
                    } else {
                        // token is not available -> register for token updates
                        UIApplication.sharedApplication.registerForRemoteNotifications()
                    }
                }
            }
    }

    companion object {
        const val FCM_TOKEN_KEY = "FCM_TOKEN"
    }
}
