package com.example.core.presentation.composableUtil

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import com.example.core.presentation.ext.toDeviceConfiguration
import com.example.core.presentation.util.DeviceConfiguration

@Composable
fun currentDeviceConfiguration(): DeviceConfiguration {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return windowSizeClass.toDeviceConfiguration()
}