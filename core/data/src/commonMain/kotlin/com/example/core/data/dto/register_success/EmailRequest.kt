package com.example.core.data.dto.register_success

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String
)