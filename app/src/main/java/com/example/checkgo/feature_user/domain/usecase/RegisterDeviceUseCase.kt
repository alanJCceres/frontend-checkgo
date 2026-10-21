package com.example.checkgo.feature_user.domain.usecase

import com.example.checkgo.core.data.dto.DeviceRequestDto
import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.core.data.repository.SharedRepository
import io.ktor.client.call.body
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val repository: SharedRepository
) {
    suspend operator fun invoke(request: DeviceRequestDto):Result<String>{
        try{
            val response = repository.putAndroidId(request)
            return when(response.status.value){
                204->{
                    Result.success("Registro de dispositivo correctamente.")
                }
                400 -> Result.failure(Exception("datos inválidos, por favor cierre la app y vuelva a ingresar"))
                409 -> {
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