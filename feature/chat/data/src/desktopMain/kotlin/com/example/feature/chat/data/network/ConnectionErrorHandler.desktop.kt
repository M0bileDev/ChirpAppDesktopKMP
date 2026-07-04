package com.example.feature.chat.data.network

import com.example.feature.chat.domain.model.ConnectionState

actual class ConnectionErrorHandler {
    actual fun getConnectionStateFromError(cause: Throwable): com.example.feature.chat.domain.model.ConnectionState {
        return ConnectionState.ERROR_NETWORK
    }

    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return true
    }
}