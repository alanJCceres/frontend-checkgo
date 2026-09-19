package com.example.checkgo.feature_auth.domain.usecase

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.core.data.localStorage.TokenManager
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import com.example.checkgo.feature_auth.data.dto.LoginUserResponseDto
import com.example.checkgo.feature_auth.data.repository.AuthRepository
import io.ktor.client.call.body
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(request: LoginUserRequestDto):Result<String>{
        try{
            val response = repository.postLoginUser(request)
            return when(response.status.value){
                200->{
                    val successBody = response.body<LoginUserResponseDto>()
                    tokenManager.saveTokens(
                        accessToken = successBody.accessToken,
                        refreshToken = successBody.refreshToken,
                        role = successBody.role.toString()
                    )
                    Result.success(successBody.role.toString())
                }
                400,401,409 -> {
                    val errorBody = response.body<ErrorResponseDto>()
                    Result.failure(Exception(errorBody.message))
                }
                else -> Result.failure(Exception("500: Error inesperado del servidor"))
            }
        }catch(e: Exception){
            return Result.failure(Exception("fallo de red o de proceso: ${e.message}"))
        }
    }
}