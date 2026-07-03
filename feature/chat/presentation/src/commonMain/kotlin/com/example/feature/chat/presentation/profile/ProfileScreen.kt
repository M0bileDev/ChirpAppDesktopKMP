@file:OptIn(ExperimentalUuidApi::class)

package com.example.feature.chat.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.chat.presentation.generated.resources.Res
import chirpappkmp.feature.chat.presentation.generated.resources.cancel
import chirpappkmp.feature.chat.presentation.generated.resources.contact_chirp_support_change_email
import chirpappkmp.feature.chat.presentation.generated.resources.current_password
import chirpappkmp.feature.chat.presentation.generated.resources.delete
import chirpappkmp.feature.chat.presentation.generated.resources.delete_profile_image
import chirpappkmp.feature.chat.presentation.generated.resources.delete_profile_image_description
import chirpappkmp.feature.chat.presentation.generated.resources.email
import chirpappkmp.feature.chat.presentation.generated.resources.new_password
import chirpappkmp.feature.chat.presentation.generated.resources.password
import chirpappkmp.feature.chat.presentation.generated.resources.password_change_successful
import chirpappkmp.feature.chat.presentation.generated.resources.password_hint
import chirpappkmp.feature.chat.presentation.generated.resources.profile_image
import chirpappkmp.feature.chat.presentation.generated.resources.save
import chirpappkmp.feature.chat.presentation.generated.resources.upload_icon
import chirpappkmp.feature.chat.presentation.generated.resources.upload_image
import com.example.core.designsystem.components.avatar.AvatarSize
import com.example.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.example.core.designsystem.components.brand.ChirpHorizontalDivider
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.dialogs.ChirpAdaptiveDialogSheetLayout
import com.example.core.designsystem.components.dialogs.DestructiveConfirmationDialog
import com.example.core.designsystem.components.textfields.ChirpPasswordTextField
import com.example.core.designsystem.components.textfields.ChirpTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.util.UiText
import com.example.core.presentation.util.clearFocusOnTap
import com.example.feature.chat.presentation.profile.components.ProfileHeaderSection
import com.example.feature.chat.presentation.profile.components.ProfileSectionLayout
import com.example.feature.chat.presentation.profile.mediapicker.rememberImagePicker
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun ProfileRoot(
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val launcher = rememberImagePicker { pickedImageData ->
        viewModel.onAction(
            ProfileAction.OnPictureSelected(
                pickedImageData.bytes,
                pickedImageData.mimeType
            )
        )
    }

    ChirpAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ProfileScreen(
            state = state,
            onAction = { action ->
                when (action) {
                    is ProfileAction.OnDismiss -> onDismiss()
                    is ProfileAction.OnUploadPictureClick -> launcher.launch()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}

@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit
) = with(state) {

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        ProfileHeaderSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            username = username,
            onCloseClick = { onAction(ProfileAction.OnDismiss) }
        )
        ChirpHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.profile_image)
        ) {
            Row {
                ChirpAvatarPhoto(
                    displayText = userInitials,
                    size = AvatarSize.LARGE,
                    imageUrl = profilePictureUrl,
                    onClick = {
                        onAction(ProfileAction.OnUploadPictureClick)
                    }
                )
                Spacer(modifier = Modifier.width(20.dp))
                FlowRow(
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ChirpButton(
                        text = stringResource(Res.string.upload_image),
                        onClick = {
                            onAction(ProfileAction.OnUploadPictureClick)
                        },
                        style = ChirpButtonStyle.SECONDARY,
                        enabled = !isUploadingImage && !isDeletingImage,
                        isLoading = isUploadingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.upload_icon),
                                contentDescription = stringResource(Res.string.upload_image)
                            )
                        }
                    )
                    ChirpButton(
                        text = stringResource(Res.string.delete),
                        onClick = {
                            onAction(ProfileAction.OnDeletePictureClick)
                        },
                        style = ChirpButtonStyle.DESTRUCTIVE_SECONDARY,
                        enabled = !isUploadingImage && !isDeletingImage && profilePictureUrl != null,
                        isLoading = isDeletingImage,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.delete)
                            )
                        }
                    )
                }
            }

            if (imageError != null) {
                Text(
                    text = imageError.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        ChirpHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.email)
        ) {
            ChirpTextField(
                state = emailTextState,
                // email address cannot be changed by user
                enabled = false,
                supportingText = stringResource(Res.string.contact_chirp_support_change_email)
            )
        }
        ChirpHorizontalDivider()
        ProfileSectionLayout(
            headerText = stringResource(Res.string.password)
        ) {
            ChirpPasswordTextField(
                state = currentPasswordTextState,
                isPasswordVisible = isCurrentPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleCurrentPasswordVisibility)
                },
                placeholder = stringResource(Res.string.current_password),
                isError = newPasswordError != null,
            )
            ChirpPasswordTextField(
                state = newPasswordTextState,
                isPasswordVisible = isNewPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(ProfileAction.OnToggleNewPasswordVisibility)
                },
                placeholder = stringResource(Res.string.new_password),
                isError = newPasswordError != null,
                supportingText = newPasswordError?.asString()
                    ?: stringResource(Res.string.password_hint)
            )
            if (isPasswordChangedSuccessful) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.password_change_successful),
                    color = MaterialTheme.colorScheme.extended.success,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.End,

                    )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
            ) {
                ChirpButton(
                    text = stringResource(Res.string.cancel),
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {
                        onAction(ProfileAction.OnDismiss)
                    }
                )
                ChirpButton(
                    text = stringResource(Res.string.save),
                    onClick = {
                        onAction(ProfileAction.OnChangePasswordClick)
                    },
                    enabled = canChangePassword,
                    isLoading = isChangingPassword
                )
            }
        }
    }

    if (showDeleteImageConfirmationDialog) {
        DestructiveConfirmationDialog(
            title = stringResource(Res.string.delete_profile_image),
            description = stringResource(Res.string.delete_profile_image_description),
            confirmationButtonText = stringResource(Res.string.delete),
            cancelButtonText = stringResource(Res.string.cancel),
            onConfirmClick = {
                onAction(ProfileAction.OnConfirmDeleteClick)
            },
            onCancelClick = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            },
            onDismiss = {
                onAction(ProfileAction.OnDismissDeleteConfirmationDialogClick)
            }
        )
    }
}

@Preview
@Composable
fun PreviewProfileScreen() {
    ChirpTheme {
        ProfileScreen(
            state = ProfileState(
                username = "Lorem ipsum",
                imageError = UiText.DynamicString("Lorem ipsum"),
                emailTextState = TextFieldState(initialText = "lorem@ipsum.com"),
                currentPasswordTextState = TextFieldState(initialText = "123456"),
                isCurrentPasswordVisible = true,
                newPasswordError = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkProfileScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        ProfileScreen(
            state = ProfileState(
                username = "Lorem ipsum",
                imageError = UiText.DynamicString("Lorem ipsum"),
                emailTextState = TextFieldState(initialText = "lorem@ipsum.com"),
                currentPasswordTextState = TextFieldState(initialText = "123456"),
                isCurrentPasswordVisible = true,
                isPasswordChangedSuccessful = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewProfileScreenDeleteProfileImageDialog() {
    ChirpTheme {
        ProfileScreen(
            state = ProfileState(
                username = "Lorem ipsum",
                imageError = UiText.DynamicString("Lorem ipsum"),
                emailTextState = TextFieldState(initialText = "lorem@ipsum.com"),
                currentPasswordTextState = TextFieldState(initialText = "123456"),
                isCurrentPasswordVisible = true,
                newPasswordError = UiText.DynamicString("Lorem ipsum"),
                showDeleteImageConfirmationDialog = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkProfileScreenDeleteProfileImageDialog() {
    ChirpTheme(
        darkTheme = true
    ) {
        ProfileScreen(
            state = ProfileState(
                username = "Lorem ipsum",
                imageError = UiText.DynamicString("Lorem ipsum"),
                emailTextState = TextFieldState(initialText = "lorem@ipsum.com"),
                currentPasswordTextState = TextFieldState(initialText = "123456"),
                isCurrentPasswordVisible = true,
                showDeleteImageConfirmationDialog = true
            ),
            onAction = {}
        )
    }
}


