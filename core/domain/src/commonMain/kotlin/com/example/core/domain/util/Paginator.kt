package com.example.core.domain.util

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

class Paginator<Key, Item>(
    // representation of initial generic key
    private val initialKey: Key,
    // function type that notifies when load get started
    private val onLoadUpdated: (Boolean) -> Unit,
    // function type that defines how request looks like
    private val onRequest: suspend (nextKey: Key) -> Result<List<Item>, DataError>,
    // function type that returns next key based on list of items
    private val getNextKey: suspend (List<Item>) -> Key,
    private val onError: suspend (Throwable?) -> Unit,
    private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) {
    private var currentKey = initialKey
    private var isOngoingRequest = false
    private var lastRequestKey: Key? = null

    suspend fun loadNextItems() {
        if (isOngoingRequest) return

        val tryToCallMoreThenOnce = currentKey == lastRequestKey
        if (currentKey != null && tryToCallMoreThenOnce) return

        isOngoingRequest = true
        // notify ui to show loading
        onLoadUpdated(true)

        try {
            onRequest(currentKey)
                .onSuccess { items ->
                    val nextKey = getNextKey(items)

                    onSuccess(items, nextKey)
                    currentKey = nextKey
                    lastRequestKey = currentKey
                }.onFailure { error ->
                    onError(PaginationErrorException(error))
                }
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            onError(e)
        } finally {
            // notify ui to hide loading
            onLoadUpdated(false)
            isOngoingRequest = false
        }
    }

    fun reset() {
        currentKey = initialKey
        lastRequestKey = null
    }
}