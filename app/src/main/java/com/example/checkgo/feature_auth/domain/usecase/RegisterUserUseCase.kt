package com.example.checkgo.feature_auth.domain.usecase

import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import com.example.checkgo.feature_auth.data.repository.AuthRepository
import io.ktor.client.call.body

class RegisterUserUseCase {
    private val repository = AuthRepository()
    suspend operator fun invoke(request: RegisterUserRequestDto):Result<String>{
        try{
            val response = repository.postRegisterUser(request)
            return when(response.status.value){
                201,200->{
                    //val body = response.body<RegisterUserRequestDto>()
                    Result.success("usuario creado")
                }
                400 -> Result.failure(Exception("datos invalidos"))
                else -> Result.failure(Exception("Error inesperado"))
            }
        }catch(e: Exception){
            return Result.failure(Exception("fallo de red: ${e.message}"))
        }
    }
}

// 1. GET (Con Store5 para caché y reactividad)
//class GetUserUseCase {
//    private val store = StoreProviders.userProfileStore
//
//    operator fun invoke(userId: String, forceRefresh: Boolean = false): Flow<StoreReadResponse<User>> {
//        return store.stream(
//            StoreRequest.cached(key = userId, refresh = forceRefresh)
//        )
//    }
//}