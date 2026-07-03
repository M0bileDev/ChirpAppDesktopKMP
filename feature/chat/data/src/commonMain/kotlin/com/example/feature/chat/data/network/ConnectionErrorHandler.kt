package com.example.feature.chat.data.network

import com.example.feature.chat.domain.model.ConnectionState

expect class ConnectionErrorHandler {
    fun getConnectionStateFromError(cause: Throwable) : ConnectionState
    fun transformException(exception: Throwable): Throwable
    fun isRetriableError(cause: Throwable): Boolean
}