package com.example.feature.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.email
import chirpappkmp.feature.auth.presentation.generated.resources.email_placeholder
import chirpappkmp.feature.auth.presentation.generated.resources.login
import chirpappkmp.feature.auth.presentation.generated.resources.password
import chirpappkmp.feature.auth.presentation.generated.resources.password_hint
import chirpappkmp.feature.auth.presentation.generated.resources.register
import chirpappkmp.feature.auth.presentation.generated.resources.username
import chirpappkmp.feature.auth.presentation.generated.resources.username_hint
import chirpappkmp.feature.auth.presentation.generated.resources.username_placeholder
import chirpappkmp.feature.auth.presentation.generated.resources.welcome_to_chirp
import com.example.core.designsystem.components.brand.ChirpBrandLogo
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import com.example.core.designsystem.components.layouts.ChirpSnackbarScaffoldLayout
import com.example.core.designsystem.components.textfields.ChirpPasswordTextField
import com.example.core.designsystem.components.textfields.ChirpTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterRoot(
    viewModel: RegisterViewModel = koinViewModel(),
    onRegisterSuccess: (String) -> Unit,
    onLoginClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Success -> {
                onRegisterSuccess(event.email)
            }
        }
    }

    RegisterScreen(
        state = state,
        onAction = { action ->
            when (action) {
                RegisterAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) = with(state) {
    ChirpSnackbarScaffoldLayout {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_to_chirp),
            errorText = registrationError?.asString(),
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                ChirpTextField(
                    state = usernameTextState,
                    placeholder = stringResource(Res.string.username_placeholder),
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                    title = stringResource(Res.string.username),
                    supportingText = usernameError?.asString()
                        ?: stringResource(Res.string.username_hint),
                    isError = usernameError != null,
                    onFocusChanged = {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ChirpTextField(
                    state = emailTextState,
                    placeholder = stringResource(Res.string.email_placeholder),
                    singleLine = true,
                    title = stringResource(Res.string.email),
                    supportingText = emailError?.asString(),
                    isError = emailError != null,
                    onFocusChanged = {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                ChirpPasswordTextField(
                    state = passwordTextState,
                    placeholder = stringResource(Res.string.password),
                    title = stringResource(Res.string.password),
                    supportingText = passwordError?.asString()
                        ?: stringResource(Res.string.password_hint),
                    isError = passwordError != null,
                    onFocusChanged = {
                        onAction(RegisterAction.OnInputTextFocusGain)
                    },
                    onToggleVisibilityClick = {
                        onAction(RegisterAction.OnTogglePasswordVisibilityClick)
                    },
                    isPasswordVisible = isPasswordVisible
                )
                Spacer(modifier = Modifier.height(16.dp))

                ChirpButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.register),
                    onClick = {
                        onAction(RegisterAction.OnRegisterClick)
                    },
                    enabled = canRegister,
                    isLoading = isRegistering
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChirpButton(
                    modifier = Modifier.fillMaxWidth(),
                    style = ChirpButtonStyle.SECONDARY,
                    text = stringResource(Res.string.login),
                    onClick = {
                        onAction(RegisterAction.OnLoginClick)
                    }
                )

            }
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    ChirpTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkRegisterScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        RegisterScreen(
            state = RegisterState(),
            onAction = {}
        )
    }
}