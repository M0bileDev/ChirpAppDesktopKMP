package com.example.feature.chat.data.network

import com.example.feature.chat.domain.model.ConnectionState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.io.EOFException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

actual class ConnectionErrorHandler {
    actual fun getConnectionStateFromError(cause: Throwable): ConnectionState {
        return when (cause) {
            is ClientRequestException,
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLException,
            is EOFException -> ConnectionState.ERROR_NETWORK

            else -> ConnectionState.ERROR_UNKNOWN
        }
    }

    actual fun transformException(exception: Throwable): Throwable {
        //do not override for android platform
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return when (cause) {
            is SocketTimeoutException,
            is WebSocketException,
            is SocketException,
            is EOFException,
            is UnknownHostException -> true

            else -> false
        }
    }
}