package com.example.checkgo.feature_auth.data.dto

import com.example.checkgo.core.data.enum.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequestDto(
    val fullname:String,
    val email:String,
    val userName:String,
    val password:String,
    val rol: UserRole,
    val planPublicId:String
    )
@Serializable
data class LoginUserRequestDto(
    val userName:String,
    val password:String
)
@Serializable
data class LoginUserResponseDto(
    val accessToken:String,
    val refreshToken:String,
    val role:UserRole
)
//@Serializable
//data class UserResponseDto(val id: Int, val name: String, val email: String) {
//    fun toDomain() = User(id = id.toString(), fullName = name, email = email) //convertir al data del form
//}