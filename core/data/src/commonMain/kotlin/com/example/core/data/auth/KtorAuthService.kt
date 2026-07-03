package com.example.core.data.auth

import com.example.core.data.dto.AuthInfoSerializable
import com.example.core.data.dto.change_password.ChangePasswordRequest
import com.example.core.data.dto.login.LoginRequest
import com.example.core.data.dto.logout.LogoutRequest
import com.example.core.data.dto.register.RegisterRequest
import com.example.core.data.dto.register_success.EmailRequest
import com.example.core.data.dto.reset_password.ResetPasswordRequest
import com.example.core.data.mappers.toDomain
import com.example.core.data.network.get
import com.example.core.data.network.post
import com.example.core.domain.auth.AuthInfo
import com.example.core.domain.auth.AuthService
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.core.domain.util.Result
import com.example.core.domain.util.map
import com.example.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {

    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(email, username, password)
        )
    }

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(email)
        )
    }

    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(email, password)
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email)
        )
    }

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/reset-password",
            body = ResetPasswordRequest(
                newPassword = newPassword,
                token = token
            )
        )
    }

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/change-password",
            body = ChangePasswordRequest(
                oldPassword = currentPassword,
                newPassword = newPassword
            )
        )
    }

    override suspend fun logout(refreshToken: String): EmptyResult<DataError.Remote> {
        return httpClient.post<LogoutRequest, Unit>(
            route = "/auth/logout",
            body = LogoutRequest(
                refreshToken = refreshToken
            )
        ).onSuccess {
            // ktor keeps bearer tokens in cache, so it has to be explicitly cleared
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
    }
}