package com.example.checkgo.feature_auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.enum.UserRole
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import com.example.checkgo.feature_auth.domain.usecase.LoginUserUseCase
import com.example.checkgo.feature_auth.domain.validators.PasswordValidator
import com.example.checkgo.feature_auth.domain.validators.UserNameValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val userName: String="",
    val password: String="",
    val isPasswordVisible: Boolean=false,

    val errorUserName: String? = null,
    val errorPassword: String? = null,
    val isLoading: Boolean=false,
)
sealed class LoginUiEvent{
    data class Navigate(val route: String): LoginUiEvent()
    data class ShowErrorToast(val message: String): LoginUiEvent()
}

class LoginViewModel: ViewModel() {
    private val loginUserUseCase = LoginUserUseCase()
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<LoginUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onUserNameChange(newUserName:String){
        _uiState.update { estadoActual ->
            estadoActual.copy(
                userName = newUserName,
                errorUserName = null
            )
        }
    }
    fun onPasswordChange(newPassword:String){
        _uiState.update { estadoActual ->
            estadoActual.copy(
                password = newPassword,
                errorPassword = null
            )
        }
    }
    fun onUserNameFocusLost(){
        val estadoActual = _uiState.value
        val validationError = UserNameValidator.validate(estadoActual.userName)
        _uiState.update { it.copy(
            errorUserName = validationError
        ) }
    }
    fun onPasswordFocusLost(){
        val estadoActual = _uiState.value
        val validationError = PasswordValidator.validate(estadoActual.password)
        _uiState.update { it.copy(
            errorPassword = validationError
        ) }
    }
    fun onLoginClicked(){
        if(!inputsInvalidos()){
            _uiState.update { it.copy(isLoading = true) }
            val login= LoginUserRequestDto(
                userName = _uiState.value.userName,
                password = _uiState.value.password
            )
            viewModelScope.launch {
                loginUserUseCase(login).fold(
                    onSuccess = { role ->
                        _uiState.update { it.copy(isLoading = false) }
                        val role = runCatching {
                            UserRole.valueOf(role)
                        }.getOrNull()

                        when (role) {
                            UserRole.SUPER_ADMIN -> {
                                _navigationEvent.send(LoginUiEvent.Navigate("homeAdmin"))
                            }
                            UserRole.USER -> {
                                _navigationEvent.send(LoginUiEvent.Navigate("homeUser"))
                            }
                            else -> {
                                _navigationEvent.send(LoginUiEvent.ShowErrorToast("Error al obtener el rol, porfavor cierre la app y vuelva a ingresar."))
                            }
                        }
                    },
                    onFailure = {exception ->
                        _uiState.update { it.copy(isLoading = false) }
                        val errorMessage = exception.message?:"Error desconocido"
                        _navigationEvent.send(LoginUiEvent.ShowErrorToast(errorMessage))
                    }
                )
            }
        }
    }
    fun inputsInvalidos(): Boolean{
        var res: Boolean=false
        val currentState = _uiState.value
        val userNameError = UserNameValidator.validate(currentState.userName)
        val passwordError = PasswordValidator.validate(currentState.password)
        if(userNameError!=null || passwordError!=null){
            onUserNameFocusLost()
            onPasswordFocusLost()
            res=true
        }
        return res
    }
    fun togglePasswordVisibility(){
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
}