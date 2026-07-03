package com.example.feature.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.auth.AuthService
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.presentation.ext.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterSuccessViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val email = savedStateHandle.get<String>("email")
        ?: throw IllegalStateException("No email passed to register success screen.")
    private val eventChannel = Channel<RegisterSuccessEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(
        RegisterSuccessState(
            registeredEmail = email
        )
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                //todo
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RegisterSuccessState()
        )

    fun onAction(registerSuccessAction: RegisterSuccessAction) {
        when (registerSuccessAction) {
            RegisterSuccessAction.OnLoginClick -> Unit
            is RegisterSuccessAction.OnResendVerificationEmailClick -> resendVerification()
        }
    }

    private fun resendVerification() = with(viewModelScope) {
        if (state.value.isResendingVerificationEmail) return@with

        _state.update {
            it.copy(
                isResendingVerificationEmail = true
            )
        }

        launch {
            authService.resendVerificationEmail(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false
                        )
                    }
                    eventChannel.send(RegisterSuccessEvent.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                            resendVerificationError = error.toUiText()
                        )
                    }
                }
        }
    }
}