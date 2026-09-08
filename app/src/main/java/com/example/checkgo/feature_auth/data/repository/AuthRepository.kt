package com.example.checkgo.feature_auth.data.repository

import com.example.checkgo.core.data.api.ApiHttp
import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import io.ktor.client.statement.HttpResponse

class AuthRepository {
    private val endpointBase:String = "api/v1/auth"
    suspend fun postRegisterUser(request: RegisterUserRequestDto): HttpResponse{
        return ApiHttp.post("${endpointBase}/register",request)
    }
    // Para GET: Devuelve el DTO ya parseado
//    suspend fun getUser(userId: String): UserResponseDto {
//        val response = ApiHttp.get("users/$userId")
//        if (response.status.value in 200..299) {
//            return response.body<UserResponseDto>()
//        } else {
//            throw Exception("Error del servidor: ${response.status.value}")
//        }
//    }

}