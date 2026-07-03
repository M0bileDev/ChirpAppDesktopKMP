package com.example.core.data.network

import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    return try {
        val response = execute()
        handleResponse(response)
    } catch (_: UnknownHostException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (_: UnresolvedAddressException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (_: ConnectException) {
        Result.Failure(DataError.Remote.NO_INTERNET)
    } catch (_: SocketTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch (_: HttpRequestTimeoutException) {
        Result.Failure(DataError.Remote.REQUEST_TIMEOUT)
    } catch (_: SerializationException) {
        Result.Failure(DataError.Remote.SERIALIZATION)
    } catch (_: Exception) {
        //kotlin cancellation exception handler -> re-throw exception when comes from coroutine
        currentCoroutineContext().ensureActive()
        Result.Failure(DataError.Remote.UNKNOWN)
    }
}