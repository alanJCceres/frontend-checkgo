package com.example.checkgo.core.data.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    @SerialName("SUPER_ADMIN")
    SUPER_ADMIN,
    @SerialName("USER")
    USER
}