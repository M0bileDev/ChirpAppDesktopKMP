package com.example.feature.auth.presentation.email_verification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirpappkmp.feature.auth.presentation.generated.resources.Res
import chirpappkmp.feature.auth.presentation.generated.resources.close
import chirpappkmp.feature.auth.presentation.generated.resources.email_verified_failed
import chirpappkmp.feature.auth.presentation.generated.resources.email_verified_failed_description
import chirpappkmp.feature.auth.presentation.generated.resources.email_verified_successfully
import chirpappkmp.feature.auth.presentation.generated.resources.email_verified_successfully_description
import chirpappkmp.feature.auth.presentation.generated.resources.login
import chirpappkmp.feature.auth.presentation.generated.resources.verifying_account
import com.example.core.designsystem.components.brand.ChirpFailureIcon
import com.example.core.designsystem.components.brand.ChirpSuccessIcon
import com.example.core.designsystem.components.buttons.ChirpButton
import com.example.core.designsystem.components.buttons.ChirpButtonStyle
import com.example.core.designsystem.components.layouts.ChirpAdaptiveResultLayout
import com.example.core.designsystem.components.layouts.ChirpSimpleResultLayout
import com.example.core.designsystem.theme.ChirpTheme
import com.example.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EmailVerificationRoot(
    viewModel: EmailVerificationViewModel = koinViewModel(),
    onLoginClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailVerificationScreen(
        state = state,
        onAction = { action ->
            when (action) {
                EmailVerificationAction.OnCloseClick -> onCloseClick()
                EmailVerificationAction.OnLoginClick -> onLoginClick()
            }
        }
    )
}

@Composable
fun EmailVerificationScreen(
    state: EmailVerificationState,
    onAction: (EmailVerificationAction) -> Unit
) = with(state) {
    ChirpAdaptiveResultLayout {
        when {
            isVerifying -> {
                VerifyingContent()
            }

            isVerified -> {
                ChirpSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_successfully_description),
                    icon = {
                        ChirpSuccessIcon()
                    },
                    primaryButton = {
                        ChirpButton(
                            text = stringResource(Res.string.login),
                            onClick = {
                                onAction(EmailVerificationAction.OnLoginClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                )
            }

            else -> {
                ChirpSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_failed),
                    description = stringResource(Res.string.email_verified_failed_description),
                    icon = {
                        Spacer(modifier = Modifier.height(32.dp))
                        ChirpFailureIcon(
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    },
                    primaryButton = {
                        ChirpButton(
                            text = stringResource(Res.string.close),
                            onClick = {
                                onAction(EmailVerificationAction.OnCloseClick)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = ChirpButtonStyle.SECONDARY
                        )
                    }

                )
            }
        }
    }
}

@Composable
fun VerifyingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .heightIn(min = 200.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.verifying_account),
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerifyingContent() {
    ChirpTheme {
        VerifyingContent()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewDarkVerifyingContent() {
    ChirpTheme {
        VerifyingContent()
    }
}

@Preview
@Composable
fun PreviewEmailVerificationScreenFailed() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewEmailVerificationScreenSuccess() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewEmailVerificationScreenVerifying() {
    ChirpTheme {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkEmailVerificationScreenFailed() {
    ChirpTheme(
        darkTheme = true
    ) {
        EmailVerificationScreen(
            state = EmailVerificationState(),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkEmailVerificationScreenSuccess() {
    ChirpTheme(
        darkTheme = true
    ) {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerified = true
            ),
            onAction = {}
        )
    }
}

@Preview
@Composable
fun PreviewDarkEmailVerificationScreenVerifying() {
    ChirpTheme(
        darkTheme = true
    ) {
        EmailVerificationScreen(
            state = EmailVerificationState(
                isVerifying = true
            ),
            onAction = {}
        )
    }
}