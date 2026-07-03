package com.example.feature.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.create_account
import chirpappkmp.feature.auth.presentation.generated.resources.email
import chirpappkmp.feature.auth.presentation.generated.resources.email_placeholder
import chirpappkmp.feature.auth.presentation.generated.resources.forgot_password
import chirpappkmp.feature.auth.presentation.generated.resources.login
import chirpappkmp.feature.auth.presentation.generated.resources.password
import chirpappkmp.feature.auth.presentation.generated.resources.welcome_back
import com.example.core.designsystem.components.brand.ChirpBrandLogo
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import com.example.core.designsystem.components.layouts.ChirpSnackbarScaffoldLayout
import com.example.core.designsystem.components.textfields.ChirpPasswordTextField
import com.example.core.designsystem.components.textfields.ChirpTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.util.ObserveAsEvents
import com.example.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.SuccessLogin -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnSignUpClick -> onCreateAccountClick()
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) = with(state) {
    ChirpSnackbarScaffoldLayout {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_back),
            errorText = error?.asString(),
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                ChirpTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailTextFieldState,
                    title = stringResource(Res.string.email),
                    placeholder = stringResource(Res.string.email_placeholder),
                    keyboardType = KeyboardType.Email,
                    singleLine = true,
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChirpPasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = passwordTextFieldState,
                    title = stringResource(Res.string.password),
                    placeholder = stringResource(Res.string.password),
                    onToggleVisibilityClick = {
                        onAction(LoginAction.OnTogglePasswordVisibilityClick)
                    },
                    isPasswordVisible = isPasswordVisible
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            onAction(LoginAction.OnForgotPasswordClick)
                        },
                    text = stringResource(Res.string.forgot_password),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(24.dp))
                ChirpButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.login),
                    enabled = canLogin,
                    isLoading = isLoggingIn,
                    onClick = {
                        onAction(LoginAction.OnLoginClick)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ChirpButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.create_account),
                    style = ChirpButtonStyle.SECONDARY,
                    onClick = {
                        onAction(LoginAction.OnSignUpClick)
                    }
                )
            }
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreen() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreenCanLogin() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(
                canLogin = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreenCanLoggingIn() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(
                isLoggingIn = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewLoginScreenError() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(
                error = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkLoginScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkLoginScreenCanLogin() {
    ChirpTheme(darkTheme = true) {
        LoginScreen(
            state = LoginState(
                canLogin = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkLoginScreenCanLoggingIn() {
    ChirpTheme(darkTheme = true) {
        LoginScreen(
            state = LoginState(
                isLoggingIn = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkLoginScreenError() {
    ChirpTheme(darkTheme = true) {
        LoginScreen(
            state = LoginState(
                error = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

