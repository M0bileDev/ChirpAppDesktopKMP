package com.example.feature.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.auth.AuthService
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val token = savedStateHandle.get<String>("token")
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                verifyEmail()
                hasLoadedInitialData = true
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EmailVerificationState()
        )

    private fun verifyEmail() = with(viewModelScope) {
        _state.update {
            it.copy(
                isVerifying = true
            )
        }

        launch {
            authService
                .verifyEmail(token ?: "Invalid token")
                .onSuccess {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = true
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = false
                        )
                    }
                }
        }
    }
}