package com.example.chirpappkmp.deeplink

import java.awt.Desktop
import javax.swing.SwingUtilities

object DesktopDeeplinkHandler {
    private var isInitialize = false
    private val supportedUriPatterns = listOf(
        Regex("^chirp://.*"),
        Regex("^https?://chirp\\.pl-coding\\.com/.*"),
    )

    fun setup() {
        if (!Desktop.isDesktopSupported()) return
        if (isInitialize) return

        try {
            val desktop = Desktop.getDesktop()
            if (!desktop.isSupported(Desktop.Action.APP_OPEN_URI)) return

            desktop.setOpenURIHandler { event ->
                val uri = event.uri.toString()
                SwingUtilities.invokeLater {
                    processUri(uri)
                }
            }
        } catch (
            e: Exception
        ) {
            e.printStackTrace()
        }
    }

    fun processUri(uri: String) {
        TODO("Not yet implemented")
    }
}