package com.example.checkgo.feature_auth.data.repository

import com.example.checkgo.core.data.api.SkipAuth
import com.example.checkgo.core.data.enum.UserRole
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import com.example.checkgo.feature_auth.presentation.viewmodel.LoginUiEvent
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val client: HttpClient
) {
    private val endpointBase:String = "api/v1/auth"
    suspend fun postRegisterUser(request: RegisterUserRequestDto): HttpResponse{
        return client.post("${endpointBase}/register") {

            val role = runCatching {
                UserRole.valueOf(request.rol.toString())
            }.getOrNull()

            when (role) {
                UserRole.SUPER_ADMIN -> {
                    attributes.put(SkipAuth, true) //si es super admin saltamos el bear token
                }
                else -> {}
            }
            setBody(request)
        }
    }
    suspend fun postLoginUser(request: LoginUserRequestDto):HttpResponse{
        return client.post("${endpointBase}/login") {
            setBody(request)
        }
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