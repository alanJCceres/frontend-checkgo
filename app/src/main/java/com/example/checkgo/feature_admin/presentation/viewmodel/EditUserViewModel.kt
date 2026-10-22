package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.bus.UserEventBus
import com.example.checkgo.feature_admin.data.dto.UserPatchRequestDto
import com.example.checkgo.feature_admin.domain.usecase.EditUserUseCase
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

data class EditUserUiState(
    val userName: String="",
    val password: String="",
    val confirmPassword: String="",
    val isPasswordVisible: Boolean=false,
    val isConfirmPasswordVisible: Boolean=false,

    val errorUserName: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val isLoading: Boolean=false,
)
sealed class EditUserUiEvent{
    data class ShowSuccessToast(val message: String): EditUserUiEvent()
    data class ShowErrorToast(val message: String): EditUserUiEvent()
}

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val editUserUseCase: EditUserUseCase,
    private val eventBus: UserEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(EditUserUiState())
    val uiState: StateFlow<EditUserUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<EditUserUiEvent>()
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

    fun onUpdateClicked() {
        val currentState = _uiState.value
        if(!inputsInvalidos()){
            _uiState.update { it.copy(isLoading = true) }
            val userToSave = UserPatchRequestDto(
                user = currentState.userName,
                password = currentState.password
            )
            viewModelScope.launch {
                editUserUseCase("123344",userToSave).fold(
                    onSuccess = { message ->
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.send(EditUserUiEvent.ShowSuccessToast(message))
                        _uiState.update { EditUserUiState() }
                        eventBus.notifyUserCreated()
                    },
                    onFailure = {exception ->
                        _uiState.update { it.copy(isLoading = false) }
                        val errorMessage = exception.message?:"Error desconocido"
                        _navigationEvent.send(EditUserUiEvent.ShowErrorToast(errorMessage))
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
        if(userNameError!=null || passwordError!=null || currentState.errorConfirmPassword!=null){
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