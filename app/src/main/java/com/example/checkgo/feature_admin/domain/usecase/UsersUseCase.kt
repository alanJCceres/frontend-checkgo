package com.example.checkgo.feature_admin.domain.usecase

import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.data.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.StoreReadResponse
import javax.inject.Inject

class UsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    operator fun invoke(forceRefresh: Boolean = false): Flow<StoreReadResponse<List<UserResponseDto>>> {
        return repository.getUsers(forceRefresh)
    }
}