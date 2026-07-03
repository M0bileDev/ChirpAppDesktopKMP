package com.example.feature.chat.data.mappers

import com.example.feature.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import com.example.feature.chat.domain.model.ProfilePictureUploadUrls

fun ProfilePictureUploadUrlsResponse.toDomain(): ProfilePictureUploadUrls {
    return ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}