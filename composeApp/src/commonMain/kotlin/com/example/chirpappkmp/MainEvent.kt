package com.example.chirpappkmp

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}