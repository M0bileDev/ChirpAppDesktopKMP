@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.example.feature.chat.data.network

import com.example.core.data.network.UrlConstants.BASE_URL_WS
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.logging.ChirpLogger
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.feature.chat.data.BuildKonfig
import com.example.feature.chat.data.dto.websocket.WebSocketMessageDto
import com.example.feature.chat.data.lifecycle.AppLifecycleObserver
import com.example.feature.chat.domain.model.ConnectionState
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

class KtorWebSocketConnector(
    private val httpClient: HttpClient,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: ChirpLogger
) {
    private var webSocketSession: WebSocketSession? = null
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState = _connectionState.asStateFlow()

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(1.seconds)
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            false
        )

    private val isInForeground = appLifecycleObserver
        .isInForeground
        .onEach { isInForeground ->
            if (isInForeground) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5_000L),
            false
        )

    val messages = combine(
        sessionStorage.observeAuthInfo(),
        isConnected,
        isInForeground
    ) { authInfo, isConnected, isInForeground ->
        when {
            authInfo == null -> {
                logger.info("No authentication details. Clearing session and disconnecting...")
                _connectionState.value = ConnectionState.DISCONNECTED
                webSocketSession?.close()
                webSocketSession = null
                connectionRetryHandler.resetDelay()
                null
            }

            !isInForeground -> {
                logger.info("Application in background, disconnecting socket proactively.")
                _connectionState.value = ConnectionState.DISCONNECTED
                webSocketSession?.close()
                webSocketSession = null
                null
            }

            !isConnected -> {
                logger.info("Device is disconnected, closing WebSocket connection.")
                _connectionState.value = ConnectionState.ERROR_NETWORK
                webSocketSession?.close()
                webSocketSession = null
                null
            }

            else -> {
                logger.info("Application in foreground and connected, establishing connection...")

                if (_connectionState.value !in listOf(
                        ConnectionState.CONNECTING,
                        ConnectionState.CONNECTED
                    )
                ) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authInfo
            }
        }
    }.flatMapLatest { authInfo ->
        if (authInfo == null) {
            emptyFlow()
        } else {
            createWebSocketFlow(authInfo.accessToken)
                // Catch and transform exceptions to platform compatibility
                .catch { throwable ->
                    logger.error("Exception in WebSocket", throwable)

                    webSocketSession?.close()
                    webSocketSession = null

                    val transformedException = connectionErrorHandler.transformException(throwable)
                    throw transformedException
                }
                // When conditions are met re-subscribe to original flow -> createWebSocketFlow
                .retryWhen { cause, attempt ->
                    logger.info("Connection failed on attempt $attempt")

                    val shouldRetry = connectionRetryHandler.shouldRetry(cause = cause)
                    if (shouldRetry) {
                        _connectionState.value = ConnectionState.CONNECTING
                        connectionRetryHandler.applyRetryDelay(attempt.toInt())
                    }

                    shouldRetry
                }
                // Catch non-retriable errors
                .catch { throwable ->
                    logger.error("Unhandled WebSocket error", throwable)

                    _connectionState.value =
                        connectionErrorHandler.getConnectionStateFromError(throwable)
                }
        }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING

        webSocketSession = httpClient.webSocketSession(
            urlString = "$BASE_URL_WS/chat"
        ) {

            header("Authorization", "Bearer $accessToken")
            header("X-API-Key", BuildKonfig.API_KEY)
        }

        webSocketSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED

            session
                .incoming
                .consumeAsFlow()
                .buffer(capacity = 100)
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info("Received raw text frame: $text")

                            val messageDto = json.decodeFromString<WebSocketMessageDto>(text)
                            send(messageDto)
                        }

                        is Frame.Ping -> {
                            logger.debug("Received ping from server, sending pong...")
                            session.send(Frame.Pong(frame.data))
                        }

                        else -> Unit
                    }
                }
        } ?: error("Failed to establish WebSocket connection")

        awaitClose {
            launch {
                withContext(NonCancellable) {
                    logger.info("Disconnecting from WebSocket session...")
                    _connectionState.value = ConnectionState.DISCONNECTED
                    webSocketSession?.close()
                    webSocketSession = null
                }
            }
        }
    }

    suspend fun sendMessage(message: String): EmptyResult<DataError.Connection> {
        if (webSocketSession == null || connectionState.value != ConnectionState.CONNECTED) {
            return Result.Failure(DataError.Connection.NOT_CONNECTED)
        }

        return try {
            webSocketSession?.send(content = message)
            Result.Success(Unit)
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            logger.error("Unable to send WebSocket message", e)
            Result.Failure(DataError.Connection.MESSAGE_SEND_FAILED)
        }
    }
}