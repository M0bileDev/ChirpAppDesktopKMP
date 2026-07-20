package com.example.feature.chat.data.network

import com.example.core.domain.logging.ChirpLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import kotlin.time.Duration.Companion.seconds

actual class ConnectivityObserver(
    private val chirpLogger: ChirpLogger
) {
    actual val isConnected: Flow<Boolean> = flow {
        while (true) {
            val connected = isConnected()
            chirpLogger.info("Desktop connectivity status: $connected")
            emit(connected)
            delay(5.seconds)
        }
    }

    // check if device support hardware network interface like Wi-Fi
    private suspend fun isConnected(): Boolean {
        val validInterface = checkAnyValidNetworkInterface()

        if (!validInterface) return false

        return connectivityTargets.any { target ->
            ping(target)
        }
    }

    private suspend fun checkAnyValidNetworkInterface(): Boolean = try {
        withContext(Dispatchers.IO) {
            NetworkInterface
                // hardware interfaces
                .getNetworkInterfaces()
        }
            .asSequence()
            .any { networkInterface ->
                // not localhost
                !networkInterface.isLoopback
                        // is enabled
                        && networkInterface.isUp
                        // an instance of an InetAddress consists of an IP address
                        // and possibly its corresponding host name
                        && networkInterface.inetAddresses.hasMoreElements()
            }
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        false
    }

    private suspend fun ping(target: InetSocketAddress): Boolean = withContext(Dispatchers.IO) {
        try {
            Socket().use {
                it.soTimeout = SOCKET_TIMEOUT
                it.connect(target)
                true
            }
        } catch (_: Exception) {
            currentCoroutineContext().ensureActive()
            false
        }
    }

    companion object {
        const val GOOGLE_DNS = "8.8.8.8"
        const val CLAUD_FLARE_DNS = "1.1.1.1"
        const val OPEN_DNS_DNS = "208.67.222.222"
        const val STANDARD_DNS_PORT = 53
        const val SOCKET_TIMEOUT = 3_000

        private val connectivityTargets = listOf(
            InetSocketAddress(GOOGLE_DNS, STANDARD_DNS_PORT),
            InetSocketAddress(CLAUD_FLARE_DNS, STANDARD_DNS_PORT),
            InetSocketAddress(OPEN_DNS_DNS, STANDARD_DNS_PORT)
        )
    }
}