package com.example.feature.auth.presentation.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibilityClick : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnLoginClick : LoginAction
    data object OnSignUpClick : LoginAction
}