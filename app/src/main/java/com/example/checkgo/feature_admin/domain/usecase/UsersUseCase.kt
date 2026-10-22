package com.example.checkgo.feature_admin.domain.usecase

import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.data.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.StoreReadResponseOrigin
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<StoreReadResponse<List<UserResponseDto>>> {
        //return repository.getUsers(forceRefresh)
        val listaHardcodeada = listOf(
            UserResponseDto(
            publicId = "123",
            fullname= "alan jesus caceres medrano",
            userName ="alan123",
            email = null,
            rol= "USER",
            active = true,
            androidId = "123",
            superAdmin = null
        ),
            UserResponseDto(
                publicId = "123",
                fullname= "Fernanda Micaela Garia Gomez",
                userName ="fers",
                email = "fers@gmail.com",
                rol= "USER",
                active = true,
                androidId = "123456",
                superAdmin = null
            )
        )

        return flowOf(
            StoreReadResponse.Data(
                value = listaHardcodeada,
                origin = StoreReadResponseOrigin.Fetcher()
            )
        )
    }
}