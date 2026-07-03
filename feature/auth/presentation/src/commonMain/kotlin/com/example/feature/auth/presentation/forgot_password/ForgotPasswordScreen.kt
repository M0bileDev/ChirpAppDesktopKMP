package com.example.feature.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.email
import chirpappkmp.feature.auth.presentation.generated.resources.email_placeholder
import chirpappkmp.feature.auth.presentation.generated.resources.forgot_password
import chirpappkmp.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully
import chirpappkmp.feature.auth.presentation.generated.resources.submit
import com.example.core.designsystem.components.brand.ChirpBrandLogo
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import com.example.core.designsystem.components.layouts.ChirpSnackbarScaffoldLayout
import com.example.core.designsystem.components.textfields.ChirpTextField
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import com.example.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordRoot(
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit
) = with(state) {
    ChirpSnackbarScaffoldLayout {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.forgot_password),
            errorText = errorText?.asString(),
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                ChirpTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = emailTextState,
                    placeholder = stringResource(Res.string.email_placeholder),
                    title = stringResource(Res.string.email),
                    isError = emailError != null,
                    supportingText = emailError?.asString(),
                    keyboardType = KeyboardType.Email,
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                ChirpButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(Res.string.submit),
                    onClick = { onAction(ForgotPasswordAction.OnSubmitClick) },
                    enabled = !isLoading && canSubmit,
                    isLoading = isLoading
                )
                Spacer(Modifier.height(16.dp))
                if (isEmailSendSuccessfully) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = stringResource(Res.string.forgot_password_email_sent_successfully),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.extended.success,
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreen() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreenError() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                errorText = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreenEmailError() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                emailError = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreenEnabled() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                canSubmit = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewForgotPasswordScreenEmailSendSuccessfully() {
    ChirpTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                isEmailSendSuccessfully = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkForgotPasswordScreen() {
    ChirpTheme(darkTheme = true) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkForgotPasswordScreenError() {
    ChirpTheme(darkTheme = true) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                errorText = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkForgotPasswordScreenEmailError() {
    ChirpTheme(darkTheme = true) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                emailError = UiText.DynamicString("Lorem ipsum")
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkForgotPasswordScreenEnabled() {
    ChirpTheme(darkTheme = true) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                canSubmit = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDarkForgotPasswordScreenEmailSendSuccessfully() {
    ChirpTheme(
        darkTheme = true
    ) {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                isEmailSendSuccessfully = true
            ),
            onAction = {}
        )
    }
}