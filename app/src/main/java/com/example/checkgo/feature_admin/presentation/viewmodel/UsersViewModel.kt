package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.domain.usecase.UsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.mobilenativefoundation.store.store5.StoreReadResponse
import javax.inject.Inject
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<UserResponseDto>) : UserUiState()
    object Empty : UserUiState()
    data class ErrorScreen(val message: String) : UserUiState()
}
@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersUseCase: UsersUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    var toastMessage by mutableStateOf<String?>(null)
        private set

    init {
        cargarUsuarios()
    }
    fun cargarUsuarios(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            usersUseCase(forceRefresh).collect { response ->
                when (response) {
                    is StoreReadResponse.Loading -> {
                        if (_uiState.value !is UserUiState.Success) {
                            _uiState.value = UserUiState.Loading
                        }
                    }
                    is StoreReadResponse.Data -> {
                        val users = response.value
                        _uiState.value = if (users.isEmpty()) {
                            UserUiState.Empty
                        } else {
                            UserUiState.Success(users)
                        }
                    }
                    is StoreReadResponse.Error -> {
                        val errorMsg = response.errorMessageOrNull() ?: "Error desconocido"
                        toastMessage = errorMsg
                        _uiState.value = UserUiState.ErrorScreen(errorMsg)
                    }
                    else -> {}
                }
            }
        }
    }
    fun dismissToast() {
        toastMessage = null
    }
}
