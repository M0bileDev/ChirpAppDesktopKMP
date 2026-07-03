package com.example.feature.auth.presentation.login

sealed interface LoginEvent {
    data object SuccessLogin : LoginEvent
}