package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

const val ITEMS_COUNT_TO_START_PAGINATION = 5

@Composable
fun PaginationScrollListener(
    lazyListState: LazyListState,
    itemsCount: Int,
    isPaginationLoading: Boolean,
    isEndReached: Boolean,
    onNearTop: () -> Unit
) {
    val updatedItemsCount by rememberUpdatedState(itemsCount)
    val updatedPaginationLoading by rememberUpdatedState(isPaginationLoading)
    val updatedEndReached by rememberUpdatedState(isEndReached)

    // local state, that keeps reference of itemsCount last time triggered pagination
    // items count = 20, local state = 0 -> start pagination
    var lastTriggerItemsCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            // items count visible in view port
            val total = info.totalItemsCount
            val topVisibleIndex = info.visibleItemsInfo.lastOrNull()?.index
            val remainingItems = if (topVisibleIndex != null) {
                total - topVisibleIndex - 1
            } else null

            // rule that describe when pagination should be triggered
            val shouldTriggerPagination = remainingItems != null
                    && remainingItems <= ITEMS_COUNT_TO_START_PAGINATION
                    && !updatedPaginationLoading
                    && !updatedEndReached

            PaginationScrollState(
                currentItemsCount = updatedItemsCount,
                shouldTriggerPagination = shouldTriggerPagination
            )
        }
            .distinctUntilChanged()
            .collect { (currentItemsCount, shouldTriggerPagination) ->
                val shouldTrigger =
                    shouldTriggerPagination && currentItemsCount > lastTriggerItemsCount

                // successfully fetched data -> 20 items, not updated list yet, could try pagination again but
                if (shouldTrigger) {
                    // currentItemsCount = 20, lastTriggerItemsCount = 20, so currentItemsCount > lastTriggerItemsCount -> false,
                    // next time shouldTrigger -> false
                    lastTriggerItemsCount = itemsCount
                    onNearTop()
                }
            }
    }
}

data class PaginationScrollState(
    val currentItemsCount: Int,
    val shouldTriggerPagination: Boolean
)