package com.example.core.data.util

enum class DesktopOS {
    WINDOWS,
    MACOS,
    LINUX
}

val currentOS: DesktopOS
    get() {
        val osName = System.getProperty("os.name").lowercase()

        return when {
            osName.contains("win") -> DesktopOS.WINDOWS
            osName.contains("mac") -> DesktopOS.MACOS
            else -> DesktopOS.LINUX
        }
    }