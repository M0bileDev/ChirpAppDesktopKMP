package com.example.chirpappkmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.chirpappkmp.di.initKoin

fun main(){
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "ChirpDesktop"
        ){
            App()
        }
    }
}