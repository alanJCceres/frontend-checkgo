package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.enum.UserRole
import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import com.example.checkgo.feature_auth.domain.usecase.RegisterUserUseCase
import com.example.checkgo.feature_auth.domain.validators.FullNameValidator
import com.example.checkgo.feature_auth.domain.validators.PasswordValidator
import com.example.checkgo.feature_auth.domain.validators.UserNameValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUserUiState(
    val fullname: String="",
    val userName: String="",
    val password: String="",
    val confirmPassword: String="",
    val isPasswordVisible: Boolean=false,
    val isConfirmPasswordVisible: Boolean=false,

    val errorFullName: String? = null,
    val errorUserName: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val isLoading: Boolean=false,
)
sealed class RegisterUserUiEvent{
    data class ShowSuccessToast(val message: String): RegisterUserUiEvent()
    data class ShowErrorToast(val message: String): RegisterUserUiEvent()
}
@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUserUiState())
    val uiState: StateFlow<RegisterUserUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<RegisterUserUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onFullnameChange(newFullname:String){
        _uiState.update { estadoActual ->
            estadoActual.copy(
                fullname = newFullname,
                errorFullName = null
            )
        }
    }
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
    fun onConfirmPasswordChange(newConfirmPassword:String){
        _uiState.update { estadoActual ->
            val coincidenPassword = estadoActual.password == newConfirmPassword
            val error =when{
                newConfirmPassword.isEmpty() -> null
                coincidenPassword -> null
                !coincidenPassword -> "Las contraseñas no coinciden."
                else -> null
            }
            estadoActual.copy(
                confirmPassword = newConfirmPassword,
                errorConfirmPassword = error
            )
        }
    }
    fun onFullnameFocusLost(){
        val estadoActual = _uiState.value
        val validationError = FullNameValidator.validate(estadoActual.fullname)
        _uiState.update { it.copy(
            errorFullName = validationError
        ) }
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
    fun onRegisterClicked() {
        val currentState = _uiState.value
        if(!inputsInvalidos()){
            _uiState.update { it.copy(isLoading = true) }
            val userToSave = RegisterUserRequestDto(
                fullname = currentState.fullname,
                userName = currentState.userName,
                password = currentState.password,
                rol = UserRole.USER,
            )
            viewModelScope.launch {
                registerUserUseCase(userToSave).fold(
                    onSuccess = { message ->
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.send(RegisterUserUiEvent.ShowSuccessToast(message))
                        _uiState.update { RegisterUserUiState() }
                    },
                    onFailure = {exception ->
                        _uiState.update { it.copy(isLoading = false) }
                        val errorMessage = exception.message?:"Error desconocido"
                        _navigationEvent.send(RegisterUserUiEvent.ShowErrorToast(errorMessage))
                    }
                )
            }
        }

    }
    fun inputsInvalidos(): Boolean{
        var res: Boolean=false
        val currentState = _uiState.value
        val fullnameError = FullNameValidator.validate(currentState.fullname)
        val userNameError = UserNameValidator.validate(currentState.userName)
        val passwordError = PasswordValidator.validate(currentState.password)
        if(fullnameError!=null || userNameError!=null ||
            passwordError!=null || currentState.errorConfirmPassword!=null){
            res=true
        }
        return res
    }

    fun togglePasswordVisibility(){
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }
    fun toggleConfirmPasswordVisibility(){
        _uiState.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }
}