package com.example.feature.chat.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.net.InetSocketAddress

actual class ConnectivityObserver {
    actual val isConnected: Flow<Boolean>
        get() = flowOf(true)

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