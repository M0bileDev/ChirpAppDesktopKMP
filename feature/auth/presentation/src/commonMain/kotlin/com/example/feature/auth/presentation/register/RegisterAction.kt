package com.example.feature.auth.presentation.register

sealed interface RegisterAction{
    data object OnInputTextFocusGain: RegisterAction
    data object OnLoginClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnTogglePasswordVisibilityClick: RegisterAction
}