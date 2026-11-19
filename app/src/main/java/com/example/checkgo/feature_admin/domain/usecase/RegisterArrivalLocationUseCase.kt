package com.example.checkgo.feature_admin.domain.usecase

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.feature_admin.data.dto.CreateLocationRequestDto
import com.example.checkgo.feature_admin.data.repository.ArrivalLocationRepository
import io.ktor.client.call.body
import javax.inject.Inject

class RegisterArrivalLocationUseCase @Inject constructor(
    private val repository: ArrivalLocationRepository
) {
    suspend operator fun invoke(request: CreateLocationRequestDto):Result<String>{
        try{
            val response = repository.postArrivalLocation(request)
            return when(response.status.value){
                201,200->{
                    Result.success("Punto de llegada creado correctamente.")
                }
                400 -> {
                    val errorBody = response.body<ErrorResponseDto>()
                    Result.failure(Exception("datos inválidos, por favor cierre la app y vuelva a ingresar"))}
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