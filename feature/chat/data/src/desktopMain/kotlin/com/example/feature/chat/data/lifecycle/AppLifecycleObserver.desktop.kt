package com.example.feature.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class AppLifecycleObserver {
    actual val isInForeground: Flow<Boolean>
        // Fewer restrictions than mobile side
        get() = flowOf(true)
}