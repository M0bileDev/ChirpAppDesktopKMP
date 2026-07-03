package com.example.feature.chat.data.notification

import com.example.core.data.network.delete
import com.example.core.data.network.post
import com.example.core.domain.util.DataError
import com.example.core.domain.util.EmptyResult
import com.example.feature.chat.data.dto.request.RegisterDeviceTokenRequest
import com.example.feature.chat.domain.notification.DeviceTokenService
import io.ktor.client.HttpClient

class KtorDeviceTokenService(
    private val httpClient: HttpClient
) : DeviceTokenService {

    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/notification/$token"
        )
    }
}