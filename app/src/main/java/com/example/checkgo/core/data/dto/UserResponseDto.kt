package com.example.checkgo.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val publicId: String,
    val fullname: String,
    val userName: String,
    val email: String?,
    val rol: String,
    val active: Boolean,
    val androidId: String?,
    val superAdmin: UserResponseDto?
) {
    val initials: String
        get() {
            val words = fullname.trim().split("\\s+".toRegex())
            return when {
                words.isEmpty() || words[0].isEmpty() -> "??"
                words.size == 1 -> words[0].take(2).uppercase()
                else -> "${words[0].first()}${words[1].first()}".uppercase()
            }
        }
}