package com.example.core.presentation.ext

import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_EXPANDED_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.example.core.presentation.util.DeviceConfiguration

fun WindowSizeClass.toDeviceConfiguration(): DeviceConfiguration {
    return when {
        minWidthDp < WIDTH_DP_MEDIUM_LOWER_BOUND && minHeightDp >= HEIGHT_DP_MEDIUM_LOWER_BOUND -> DeviceConfiguration.MOBILE_PORTRAIT
        minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND && minHeightDp < HEIGHT_DP_MEDIUM_LOWER_BOUND -> DeviceConfiguration.MOBILE_LANDSCAPE
        minWidthDp in WIDTH_DP_MEDIUM_LOWER_BOUND..WIDTH_DP_EXPANDED_LOWER_BOUND && minHeightDp >= HEIGHT_DP_EXPANDED_LOWER_BOUND -> DeviceConfiguration.TABLET_PORTRAIT
        minWidthDp >= WIDTH_DP_EXPANDED_LOWER_BOUND && minHeightDp in HEIGHT_DP_MEDIUM_LOWER_BOUND..HEIGHT_DP_EXPANDED_LOWER_BOUND -> DeviceConfiguration.TABLET_LANDSCAPE
        else -> DeviceConfiguration.DESKTOP

    }
}