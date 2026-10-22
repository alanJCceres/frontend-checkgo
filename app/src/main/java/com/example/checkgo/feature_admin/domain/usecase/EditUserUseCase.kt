package com.example.checkgo.feature_admin.domain.usecase

import com.example.checkgo.core.data.dto.ErrorResponseDto
import com.example.checkgo.feature_admin.data.dto.UserPatchRequestDto
import com.example.checkgo.feature_admin.data.repository.UsersRepository
import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import io.ktor.client.call.body
import javax.inject.Inject

class EditUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(idUser:String,request: UserPatchRequestDto):Result<String>{
        try{
            val response = repository.patchEditUser(idUser,request)
            return when(response.status.value){
                201,200->{
                    Result.success("usuario actualizado correctamente.")
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