package com.example.checkgo.feature_admin.domain.usecase

import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.data.repository.UsersRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(userPublicId: String): UserResponseDto?{
        return usersRepository.getUserById(userPublicId)
    }
}