package com.example.checkgo.feature_admin.data.dto

import kotlinx.serialization.Serializable

@Serializable // Si usas kotlinx.serialization o @JsonProperty si usas Jackson/Gson
data class UserPatchRequestDto(
    val user: String? = null,
    val password: String? = null
)