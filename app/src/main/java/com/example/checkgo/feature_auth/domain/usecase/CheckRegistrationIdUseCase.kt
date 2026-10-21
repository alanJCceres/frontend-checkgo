package com.example.checkgo.feature_auth.domain.usecase

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.core.data.localStorage.AppDataStoreManager
import com.example.checkgo.feature_auth.data.repository.AuthRepository
import io.ktor.client.call.body
import javax.inject.Inject

class CheckRegistrationIdUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val appDataStoreManager: AppDataStoreManager
) {
    suspend operator fun invoke(): Result<Boolean>{
        try{
            val response = repository.getFirstTimeLogin()
            return when(response.status.value){
                200->{
                    val successBody = response.body<Boolean>()
                    appDataStoreManager.saveNeedsRegistration(successBody)
                    Result.success(successBody)
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