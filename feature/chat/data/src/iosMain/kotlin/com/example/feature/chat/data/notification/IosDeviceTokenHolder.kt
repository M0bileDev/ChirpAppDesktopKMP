package com.example.feature.chat.data.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

//Pass token from swift file to kotlin code, bridge from ios to kotlin side
object IosDeviceTokenHolder {

    private val _token = MutableStateFlow<String?>(null)
    val token = _token.asStateFlow()

    fun updateToken(token: String?) {
        _token.value = token
    }
}