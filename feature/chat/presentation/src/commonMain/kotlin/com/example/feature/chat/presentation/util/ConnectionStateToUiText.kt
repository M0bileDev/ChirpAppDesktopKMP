package com.example.feature.chat.presentation.util

import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.network_error
import chirpappkmp.feature.chat.presentation.generated.resources.offline
import chirpappkmp.feature.chat.presentation.generated.resources.online
import chirpappkmp.feature.chat.presentation.generated.resources.reconnecting
import chirpappkmp.feature.chat.presentation.generated.resources.unknown_error
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.model.ConnectionState

fun ConnectionState.toUiText(): UiText {
    val resourceId = when (this) {
        ConnectionState.DISCONNECTED -> Res.string.offline
        ConnectionState.CONNECTING -> Res.string.reconnecting
        ConnectionState.CONNECTED -> Res.string.online
        ConnectionState.ERROR_NETWORK -> Res.string.network_error
        ConnectionState.ERROR_UNKNOWN -> Res.string.unknown_error
    }

    return UiText.Resource(resourceId)
}