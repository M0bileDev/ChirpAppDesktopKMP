package com.example.feature.auth.presentation.reset_password

sealed interface ResetPasswordAction {
    data object OnSubmitClick : ResetPasswordAction
    data object OnTogglePassword : ResetPasswordAction
}