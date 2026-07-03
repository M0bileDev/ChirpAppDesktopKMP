package com.example.feature.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import com.example.core.presentation.util.UiText

data class ProfileState(
    val username: String = "",
    val userInitials: String = "--",
    val profilePictureUrl: String? = null,
    val isUploadingImage: Boolean = false,
    val isDeletingImage: Boolean = false,
    val showDeleteImageConfirmationDialog: Boolean = false,
    val imageError: UiText? = null,
    val emailTextState: TextFieldState = TextFieldState(),
    val currentPasswordTextState: TextFieldState = TextFieldState(),
    val newPasswordTextState: TextFieldState = TextFieldState(),
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isChangingPassword: Boolean = false,
    val newPasswordError: UiText? = null,
    val canChangePassword: Boolean = false,
    val isPasswordChangedSuccessful: Boolean = false
)