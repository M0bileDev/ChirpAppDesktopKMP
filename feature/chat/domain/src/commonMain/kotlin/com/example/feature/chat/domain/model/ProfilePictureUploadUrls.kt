package com.example.feature.chat.domain.model

data class ProfilePictureUploadUrls(
    // url that represents endpoint which listen for image bytes
    // (there is time limit, like 15 minutes)
    val uploadUrl: String,
    // publicly accessible url after image has been uploaded
    val publicUrl: String,
    val headers: Map<String, String>
)
