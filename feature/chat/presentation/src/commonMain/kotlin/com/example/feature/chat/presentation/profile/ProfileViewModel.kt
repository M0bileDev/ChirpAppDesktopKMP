package com.example.feature.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.error_current_password_equal_to_new_one
import chirpappkmp.feature.chat.presentation.generated.resources.error_current_password_incorrect
import chirpappkmp.feature.chat.presentation.generated.resources.error_invalid_file_type
import com.example.core.domain.auth.AuthService
import com.example.core.domain.auth.SessionStorage
import com.example.core.domain.util.DataError
import com.example.core.domain.util.onFailure
import com.example.core.domain.util.onSuccess
import com.example.core.domain.validation.PasswordValidator
import com.example.core.presentation.ext.toUiText
import com.example.core.presentation.util.UiText
import com.example.feature.chat.domain.participant.ChatParticipantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository,
    sessionStorage: SessionStorage
) : ViewModel() {
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        sessionStorage.observeAuthInfo()
    ) { currentState, authInfo ->
        authInfo?.let { info ->
            currentState.copy(
                username = info.user.username,
                emailTextState = TextFieldState(initialText = info.user.email),
                profilePictureUrl = info.user.profilePictureUrl,
                userInitials = info.user.username.take(2)
            )
        } ?: currentState
    }.onStart {
        if (!hasLoadedInitialData) {
            observeCanChangePassword()
            fetchLocalParticipantDetails()
            hasLoadedInitialData = true
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = ProfileState()
    )

    private fun observeCanChangePassword() {
        val isCurrentPasswordValidFlow = snapshotFlow {
            state.value.currentPasswordTextState.text.toString()
        }.map { currentPassword ->
            currentPassword.isNotBlank()
        }.distinctUntilChanged()

        val isNewPasswordValidFlow = snapshotFlow {
            state.value.newPasswordTextState.text.toString()
        }.map { newPassword ->
            PasswordValidator.validate(newPassword).isValidPassword
        }.distinctUntilChanged()

        combine(
            isCurrentPasswordValidFlow,
            isNewPasswordValidFlow
        ) { isCurrentPasswordValid, isNewPasswordValid ->
            _state.update {
                it.copy(
                    canChangePassword = isCurrentPasswordValid && isNewPasswordValid
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchLocalParticipantDetails() {
        viewModelScope.launch {
            chatParticipantRepository.fetchLocalParticipant()
        }
    }

    fun onAction(profileAction: ProfileAction) {
        when (profileAction) {
            ProfileAction.OnChangePasswordClick -> changePassword()
            ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            is ProfileAction.OnPictureSelected -> uploadProfilePicture(
                profileAction.bytes,
                profileAction.mimeType
            )

            ProfileAction.OnDeletePictureClick -> showDeleteConfirmation()
            ProfileAction.OnDismissDeleteConfirmationDialogClick -> dismissDeleteConfirmation()
            ProfileAction.OnConfirmDeleteClick -> deleteProfilePicture()
            else -> Unit
        }
    }

    private fun changePassword() {
        if (!state.value.canChangePassword || state.value.isChangingPassword) return

        _state.update {
            it.copy(
                isChangingPassword = true,
                isPasswordChangedSuccessful = false
            )
        }

        val currentPassword = state.value.currentPasswordTextState.text.toString()
        val newPassword = state.value.newPasswordTextState.text.toString()
        viewModelScope.launch {
            authService.changePassword(
                currentPassword = currentPassword,
                newPassword = newPassword
            ).onSuccess {
                state.value.currentPasswordTextState.clearText()
                state.value.newPasswordTextState.clearText()

                _state.update {
                    it.copy(
                        newPasswordError = null,
                        isPasswordChangedSuccessful = true
                    )
                }
            }.onFailure { error ->
                val errorMessage = when (error) {
                    DataError.Remote.UNAUTHORIZED -> {
                        UiText.Resource(Res.string.error_current_password_incorrect)
                    }

                    DataError.Remote.CONFLICT -> {
                        UiText.Resource(Res.string.error_current_password_equal_to_new_one)
                    }

                    else -> error.toUiText()
                }
                _state.update {
                    it.copy(
                        newPasswordError = errorMessage
                    )
                }
            }
        }

        _state.update {
            it.copy(
                isNewPasswordVisible = false,
                isCurrentPasswordVisible = false,
                isChangingPassword = false
            )
        }
    }

    private fun toggleCurrentPasswordVisibility() {
        _state.update {
            it.copy(
                isCurrentPasswordVisible = !it.isCurrentPasswordVisible
            )
        }
    }

    private fun toggleNewPasswordVisibility() {
        _state.update {
            it.copy(
                isNewPasswordVisible = !it.isNewPasswordVisible
            )
        }
    }

    private fun uploadProfilePicture(bytes: ByteArray, mimeType: String?) {
        if (state.value.isUploadingImage) return
        if (mimeType == null) {
            _state.update {
                it.copy(
                    imageError = UiText.Resource(Res.string.error_invalid_file_type),
                )
            }
            return
        }

        _state.update {
            it.copy(
                isUploadingImage = true,
                imageError = null
            )
        }

        viewModelScope.launch {
            chatParticipantRepository
                .uploadProfilePicture(
                    imageBytes = bytes,
                    mimeType = mimeType
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isUploadingImage = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            imageError = error.toUiText(),
                            isUploadingImage = false
                        )
                    }
                }
        }
    }

    private fun showDeleteConfirmation() {
        _state.update {
            it.copy(
                showDeleteImageConfirmationDialog = true
            )
        }
    }

    private fun dismissDeleteConfirmation() {
        _state.update {
            it.copy(
                showDeleteImageConfirmationDialog = false
            )
        }
    }

    private fun deleteProfilePicture() {
        if (state.value.isDeletingImage || state.value.profilePictureUrl == null) return

        _state.update {
            it.copy(
                isDeletingImage = true,
                imageError = null,
                showDeleteImageConfirmationDialog = false
            )
        }

        viewModelScope.launch {
            chatParticipantRepository
                .deleteProfilePicture()
                .onSuccess {
                    _state.update {
                        it.copy(
                            isDeletingImage = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isDeletingImage = false,
                            imageError = error.toUiText()
                        )
                    }
                }
        }
    }
}