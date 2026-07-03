package com.example.feature.auth.presentation.forgot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.error_invalid_email
import com.example.core.domain.auth.AuthService
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.presentation.ext.toUiText
import com.example.core.presentation.util.UiText
import com.example.feature.auth.domain.EmailValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authService: AuthService
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ForgotPasswordState()
        )

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()

    private fun observeValidationState() {
        isEmailValidFlow.onEach { isEmailValid ->
            _state.update {
                it.copy(
                    canSubmit = isEmailValid,
                )
            }
        }.launchIn(viewModelScope)
    }


    fun onAction(forgotPasswordAction: ForgotPasswordAction) {
        when (forgotPasswordAction) {
            is ForgotPasswordAction.OnSubmitClick -> submitForgotPassword()
        }
    }

    private fun submitForgotPassword() = viewModelScope.launch {
        if (state.value.isLoading || !validateFormInputs()) return@launch

        _state.update {
            it.copy(
                isLoading = true,
                isEmailSendSuccessfully = false,
                errorText = null
            )
        }

        val email = state.value.emailTextState.text.toString()
        launch {
            authService
                .forgotPassword(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isEmailSendSuccessfully = true,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorText = error.toUiText()
                        )
                    }
                }
        }
    }

    private fun validateFormInputs(): Boolean {
        val email = state.value.emailTextState.text.toString()
        val isEmailValid = EmailValidator.validate(email)
        val emailError = if (!isEmailValid) {
            UiText.Resource(Res.string.error_invalid_email)
        } else null

        _state.update {
            it.copy(
                emailError = emailError
            )
        }

        return isEmailValid
    }
}