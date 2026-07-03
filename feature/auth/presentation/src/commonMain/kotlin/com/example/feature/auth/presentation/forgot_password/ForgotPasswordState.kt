package com.example.feature.auth.presentation.forgot_password

import androidx.compose.foundation.text.input.TextFieldState
import com.example.core.presentation.util.UiText

data class ForgotPasswordState(
    val emailTextState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSendSuccessfully: Boolean = false,
    val emailError: UiText? = null,
    val canSubmit: Boolean = false
)