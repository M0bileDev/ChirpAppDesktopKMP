package com.example.feature.chat.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.NetworkInterface

actual class ConnectivityObserver {
    actual val isConnected: Flow<Boolean>
        get() = flowOf(true)

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

    companion object {
        const val GOOGLE_DNS = "8.8.8.8"
        const val CLAUD_FLARE_DNS = "1.1.1.1"
        const val OPEN_DNS_DNS = "208.67.222.222"
        const val STANDARD_DNS_PORT = 53

        private val connectivityTargets = listOf(
            InetSocketAddress(GOOGLE_DNS, STANDARD_DNS_PORT),
            InetSocketAddress(CLAUD_FLARE_DNS, STANDARD_DNS_PORT),
            InetSocketAddress(OPEN_DNS_DNS, STANDARD_DNS_PORT)
        )
    }
}