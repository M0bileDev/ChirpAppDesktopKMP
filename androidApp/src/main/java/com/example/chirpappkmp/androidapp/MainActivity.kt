package com.example.chirpappkmp.androidapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.chirpappkmp.App
import com.example.chirpappkmp.navigation.ExternalUriHandler

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var displaySplashScreen = true

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                displaySplashScreen
            }
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // handle when:
        // - app HAS BEEN launched
        // - notification has been clicked
        handleChatMessageDeepLink(intent)

        setContent {
            App(
                onAuthenticationChecked = {
                    displaySplashScreen = false
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // handle when:
        // - app HAS NOT BEEN launched
        // - notification has been clicked
        handleChatMessageDeepLink(intent)

    }

    private fun handleChatMessageDeepLink(intent: Intent) {
        val chatId = intent.getStringExtra(CHAT_ID) ?: intent.extras?.getString(CHAT_ID)
        chatId?.let {
            val deepLinkUrl = buildString { CHAT_DETAIL_URL + CHAT_ID }
            ExternalUriHandler.onNewUri(deepLinkUrl)
        }
    }

    companion object {
        const val CHAT_ID = "chatId"
        const val CHAT_DETAIL_URL = "chirp://chat_detail/"
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}