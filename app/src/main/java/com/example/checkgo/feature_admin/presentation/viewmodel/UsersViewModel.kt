package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.dto.UserResponseDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<UserResponseDto>) : UserUiState()
    object Empty : UserUiState()
    data class ShowErrorToast(val message: String): UserUiState()
}
@HiltViewModel
class UsersViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    init {
        cargarUsuarios()
    }
    fun cargarUsuarios(simulateEmpty: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UserUiState.Loading

            delay(3000)

            if (simulateEmpty) {
                _uiState.value = UserUiState.Empty
            } else {
                val mockUsers = listOf(
                    UserResponseDto(
                        publicId = "3a42602d-a977-4585-8794-c69c0ac6d0b4",
                        fullname = "alan jesus caceres medrano",
                        userName = "jesus",
                        email = null,
                        rol = "USER",
                        active = true,
                        androidId = "c47099c34209645c",
                        superAdmin = null
                    ),
                    UserResponseDto(
                        publicId = "b91122a1",
                        fullname = "Fernanda micaela garcia gomez",
                        userName = "fers",
                        email = "maria@test.com",
                        rol = "ADMIN",
                        active = false,
                        androidId = "x89901",
                        superAdmin = null
                    )
                )
                _uiState.value = UserUiState.Success(mockUsers)
            }
        }
    }
}
