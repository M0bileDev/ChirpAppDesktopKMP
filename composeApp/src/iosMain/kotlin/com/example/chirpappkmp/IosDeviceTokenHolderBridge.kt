package com.example.chirpappkmp

import com.example.feature.chat.data.notification.IosDeviceTokenHolder

//Xcode has only access to application module
//Small bridge for IosDeviceTokenHolder
object IosDeviceTokenHolderBridge {
    fun updateToken(token: String?) {
        IosDeviceTokenHolder.updateToken(token = token)
    }
}