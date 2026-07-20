package com.example.feature.chat.data.network

import com.example.feature.chat.domain.model.ConnectionState
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import kotlinx.coroutines.CancellationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

actual class ConnectionErrorHandler {
    actual fun getConnectionStateFromError(cause: Throwable): ConnectionState {
        val nsError = cause.extractNsError()

        return when {
            nsError != null && nsError.code.isNetworkError -> ConnectionState.ERROR_NETWORK
            nsError != null -> ConnectionState.ERROR_UNKNOWN
            cause.findInCauseChain<IOSNetworkCancellationException>() != null -> ConnectionState.ERROR_NETWORK
            else -> ConnectionState.ERROR_UNKNOWN
        }
    }

    actual fun transformException(exception: Throwable): Throwable {
        if (exception is CancellationException) {
            val nsError = exception.extractNsError()
            if (nsError != null && nsError.code.isNetworkError) {
                return IOSNetworkCancellationException(
                    message = "Network connection lost (extracted from cancellation)",
                    cause = exception
                )
            }
        }

        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        if (cause.findInCauseChain<IOSNetworkCancellationException>() != null) {
            return true
        }

        return cause.extractNsError()?.code?.isNetworkError == true
    }

    private fun Throwable.extractNsError(): NSError? =
        findInCauseChain<DarwinHttpRequestException>()?.origin

    private inline fun <reified T : Throwable> Throwable.findInCauseChain(): T? {
        var current: Throwable? = this
        while (current != null) {
            if (current is T) return current
            current = current.cause
        }
        return null
    }

    private val Long.isNetworkError: Boolean
        get() = this == NSURLErrorNotConnectedToInternet ||
            this == NSURLErrorNetworkConnectionLost ||
            this == NSURLErrorTimedOut
}
