package com.example.checkgo.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponseDto (
    val details: String? = null,
    val error: String? = null,
    val message: String? = null,
    val path: String? = null,
    val status: Int? = null,
    val timestamp: String? = null
)