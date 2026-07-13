package com.example.chirpappkmp.deeplink

import com.example.chirpappkmp.navigation.ExternalUriHandler
import java.awt.Desktop
import javax.swing.SwingUtilities

object DesktopDeeplinkHandler {
    private var isInitialize = false
    val supportedUriPatterns = listOf(
        Regex("^chirp://.*"),
        Regex("^https?://chirp\\.pl-coding\\.com/.*"),
    )

    fun setup() {
        if (!Desktop.isDesktopSupported()) return
        if (isInitialize) return

        try {
            val desktop = Desktop.getDesktop()
            if (!desktop.isSupported(Desktop.Action.APP_OPEN_URI)) return

            //support when process is ongoing (app works)
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
        val cleanUri = uri.trim('"',' ')
        if(!isValidUri(uri)) return

        ExternalUriHandler.onNewUri(cleanUri)
    }

    private fun isValidUri(uri: String): Boolean {
        return supportedUriPatterns.any { it.matches(uri) }
    }
}