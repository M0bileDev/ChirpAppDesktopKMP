package com.example.feature.auth.presentation.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.reset_password_error
import chirpappkmp.feature.auth.presentation.generated.resources.reset_password_error_same_password
import com.example.core.domain.auth.AuthService
import com.example.core.domain.util.DataError
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.domain.validation.PasswordValidator
import com.example.core.presentation.ext.toUiText
import com.example.core.presentation.util.UiText
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

class ResetPasswordViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val token = savedStateHandle.get<String>("token")
        ?: throw IllegalStateException("Password reset token cannot be null")
    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResetPasswordState()
        )

    private val isPasswordValidFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .map { password -> PasswordValidator.validate(password).isValidPassword }
        .distinctUntilChanged()

    private fun observeValidationState() {
        isPasswordValidFlow.onEach { isPasswordValid ->
            _state.update {
                it.copy(
                    canSubmit = isPasswordValid
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(resetPasswordAction: ResetPasswordAction) {
        when (resetPasswordAction) {
            ResetPasswordAction.OnSubmitClick -> resetPassword()
            ResetPasswordAction.OnTogglePassword -> togglePassword()
        }
    }

    private fun togglePassword() {
        _state.update {
            it.copy(
                isPasswordVisible = !state.value.isPasswordVisible
            )
        }
    }

    private fun resetPassword() = with(viewModelScope) {
        if (state.value.isLoading || !state.value.canSubmit) return@with

        _state.update {
            it.copy(
                isLoading = true,
                isResetSuccessful = false
            )
        }

        launch {
            val newPassword = state.value.passwordTextState.text.toString()
            authService.resetPassword(
                newPassword = newPassword,
                token = token
            ).onSuccess {
                _state.update {
                    it.copy(
                        isLoading = false,
                        isResetSuccessful = true,
                        errorText = null
                    )
                }
            }.onFailure { error ->
                val errorText = when (error) {
                    //token expired
                    DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.reset_password_error)
                    //new password is the same as old one
                    DataError.Remote.CONFLICT -> UiText.Resource(Res.string.reset_password_error_same_password)
                    else -> error.toUiText()
                }
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorText = errorText
                    )
                }
            }
        }
    }
}