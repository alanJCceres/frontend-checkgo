package com.example.checkgo.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.enum.UserRole
import com.example.checkgo.core.data.localStorage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
): ViewModel() {
    // Estado para mostrar una pantalla de carga mientras se leen las preferencias
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    // Estado para guardar la ruta inicial calculada
    private val _startDestination = MutableStateFlow("login")
    val startDestination = _startDestination.asStateFlow()

    init {
        checkAuthState()
    }
    private fun checkAuthState() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = tokenManager.getAccessToken()
            val role = tokenManager.getRole()

            if (!token.isNullOrEmpty() && !role.isNullOrEmpty()) {
                val role = runCatching {
                    UserRole.valueOf(role)
                }.getOrNull()

                when (role) {
                    UserRole.SUPER_ADMIN -> {
                        _startDestination.value = "homeAdmin"
                    }
                    UserRole.USER -> {
                        _startDestination.value = "homeUser"
                    }
                    else -> {
                        _startDestination.value = "login"
                    }
                }
            } else {
                _startDestination.value = "login"
            }
            _isLoading.value = false
        }
    }
}