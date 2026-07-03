package com.example.feature.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.account_successfully_created
import chirpappkmp.feature.auth.presentation.generated.resources.login
import chirpappkmp.feature.auth.presentation.generated.resources.resend_verification_email
import chirpappkmp.feature.auth.presentation.generated.resources.resent_verification_email
import chirpappkmp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import com.example.core.designsystem.components.brand.ChirpSuccessIcon
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.layouts.ChirpAdaptiveResultLayout
import com.example.core.designsystem.components.layouts.ChirpSimpleResultLayout
import com.example.core.designsystem.components.layouts.ChirpSnackbarScaffoldLayout
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.presentation.util.ObserveAsEvents
import com.example.core.presentation.util.UiText
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessRoot(
    viewModel: RegisterSuccessViewModel = koinViewModel(),
    onLoginClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            RegisterSuccessEvent.ResendVerificationEmailSuccess -> {
                snackbarHostState.showSnackbar(
                    message = getString(Res.string.resent_verification_email)
                )
            }
        }
    }

    RegisterSuccessScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = { action ->
            when (action) {
                RegisterSuccessAction.OnLoginClick -> onLoginClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun RegisterSuccessScreen(
    state: RegisterSuccessState,
    snackbarHostState: SnackbarHostState,
    onAction: (RegisterSuccessAction) -> Unit
) = with(state) {
    ChirpSnackbarScaffoldLayout(
        snackbarHostState = snackbarHostState
    ) {
        ChirpAdaptiveResultLayout {
            ChirpSimpleResultLayout(
                title = stringResource(Res.string.account_successfully_created),
                description = stringResource(
                    Res.string.verification_email_sent_to_x,
                    registeredEmail
                ),
                icon = {
                    ChirpSuccessIcon()
                },
                primaryButton = {
                    ChirpButton(
                        text = stringResource(Res.string.login),
                        onClick = {
                            onAction(RegisterSuccessAction.OnLoginClick)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                secondaryButton = {
                    ChirpButton(
                        text = stringResource(Res.string.resend_verification_email),
                        onClick = {
                            onAction(RegisterSuccessAction.OnResendVerificationEmailClick)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isResendingVerificationEmail,
                        isLoading = isResendingVerificationEmail,
                        style = ChirpButtonStyle.SECONDARY
                    )
                },
                error = resendVerificationError?.asString()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRegisterSuccessScreen() {
    ChirpTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet"
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterSuccessScreenError() {
    ChirpTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet",
                resendVerificationError = UiText.DynamicString("Lorem ipsum")
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewRegisterSuccessScreenSnackbar() {
    val coroutineScope = rememberCoroutineScope()
    ChirpTheme {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet"
            ),
            snackbarHostState = SnackbarHostState().apply {
                coroutineScope.launch {
                    showSnackbar("Lorem ipsum")
                }
            },
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkRegisterSuccessScreen() {
    ChirpTheme(
        darkTheme = true
    ) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet"
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkRegisterSuccessScreenError() {
    ChirpTheme(
        darkTheme = true
    ) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet",
                resendVerificationError = UiText.DynamicString("Lorem ipsum")
            ),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkRegisterSuccessScreenSnackbar() {
    val coroutineScope = rememberCoroutineScope()
    ChirpTheme(
        darkTheme = true
    ) {
        RegisterSuccessScreen(
            state = RegisterSuccessState(
                registeredEmail = "lorem@impsum.internet"
            ),
            snackbarHostState = SnackbarHostState().apply {
                coroutineScope.launch {
                    showSnackbar("Lorem ipsum")
                }
            },
            onAction = {}
        )
    }
}
