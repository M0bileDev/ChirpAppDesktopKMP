package com.example.feature.chat.presentation.chat_detail.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import com.example.feature.chat.presentation.model.MessageUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.seconds

@Composable
fun MessageBannerScrollListener(
    lazyListState: LazyListState,
    messages: List<MessageUi>,
    isBannerVisible: Boolean,
    onShowBanner: (topVisibleBannerIndex: Int) -> Unit,
    onHideBanner: () -> Unit
) {
    val isBannerVisibleUpdated by rememberUpdatedState(isBannerVisible)

    LaunchedEffect(lazyListState, messages) {
        snapshotFlow {
            val info = lazyListState.layoutInfo
            // items count visible in view port
            val total = info.totalItemsCount
            // items visible in view port
            val visibleItems = info.visibleItemsInfo
            val oldestVisibleMessageIndex = visibleItems.maxOfOrNull { it.index } ?: -1
            val isAtOldestMessages = oldestVisibleMessageIndex >= total - 1
            val isAtNewestMessages = visibleItems.any { it.index == 0 }

            MessageBannerScrollState(
                oldestVisibleMessageIndex = oldestVisibleMessageIndex,
                isScrollInProgress = lazyListState.isScrollInProgress,
                isAtEdgeOfList = isAtOldestMessages || isAtNewestMessages
            )
        }
            .distinctUntilChanged()
            .collect { (oldestVisibleMessageIndex, isScrollInProgress, isAtEdgeOfList) ->
                val shouldShowBanner =
                    isScrollInProgress && !isAtEdgeOfList && oldestVisibleMessageIndex >= 0

                when {
                    shouldShowBanner -> onShowBanner(oldestVisibleMessageIndex)
                    !shouldShowBanner && isBannerVisibleUpdated -> {
                        delay(1.seconds)
                        onHideBanner()
                    }
                }
            }
    }
}

data class MessageBannerScrollState(
    val oldestVisibleMessageIndex: Int,
    val isScrollInProgress: Boolean,
    val isAtEdgeOfList: Boolean
)