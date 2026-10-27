package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.bus.UserEventBus
import com.example.checkgo.feature_admin.data.dto.UserPatchRequestDto
import com.example.checkgo.feature_admin.domain.usecase.EditUserUseCase
import com.example.checkgo.feature_admin.domain.usecase.UserUseCase
import com.example.checkgo.core.common.UserNameValidator
import com.example.checkgo.feature_admin.domain.validators.NewPasswordValidator
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
    val password: String?=null,
    val confirmPassword: String="",
    val isPasswordVisible: Boolean=false,
    val isConfirmPasswordVisible: Boolean=false,

    val errorUserName: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val isLoading: Boolean=false,
    val buttonEnabled: Boolean=false
)
sealed class EditUserUiEvent{
    data class ShowSuccessToast(val message: String): EditUserUiEvent()
    data class ShowErrorToast(val message: String): EditUserUiEvent()
}

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val editUserUseCase: EditUserUseCase,
    private val userUseCase: UserUseCase,
    private val eventBus: UserEventBus,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val userPublicId: String = checkNotNull(savedStateHandle["userId"])
    private var copyUserName:String? = null
    private val _uiState = MutableStateFlow(EditUserUiState())
    val uiState: StateFlow<EditUserUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<EditUserUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = userUseCase(userPublicId)
            if (user != null) {
                _uiState.update { it.copy(userName = user.userName) }
                copyUserName = user.userName
                _uiState.update { it.copy(isLoading = false) }
                _uiState.update { it.copy(buttonEnabled = true) }
            }else{
                _navigationEvent.send(EditUserUiEvent.ShowErrorToast("Error al cargar el usuario"))
                _uiState.update { it.copy(isLoading = false) }
                _uiState.update { it.copy(buttonEnabled = false) }
            }
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
                newConfirmPassword.isEmpty() -> "Verifique la nueva contraseña."
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
        if(estadoActual.password != null){
            val validationError = NewPasswordValidator.validate(estadoActual.password)
            _uiState.update { it.copy(
                errorPassword = validationError
            ) }
        }

    }

    fun onUpdateClicked() {
        val currentState = _uiState.value
        if(!inputsInvalidos() && (currentState.password != null || (currentState.userName != copyUserName))){
            _uiState.update { it.copy(isLoading = true) }
            _uiState.update { it.copy(buttonEnabled = false) }
            val userToEdit = UserPatchRequestDto(
                userName = if(currentState.userName != copyUserName)currentState.userName else null,
                password = currentState.password
            )
            viewModelScope.launch {
                editUserUseCase(userPublicId,userToEdit).fold(
                    onSuccess = { message ->
                        _uiState.update { it.copy(isLoading = false) }
                        _uiState.update { it.copy(buttonEnabled = true) }
                        copyUserName = currentState.userName
                        _navigationEvent.send(EditUserUiEvent.ShowSuccessToast(message))
                        eventBus.notifyUserCreated()
                    },
                    onFailure = {exception ->
                        _uiState.update { it.copy(isLoading = false) }
                        _uiState.update { it.copy(buttonEnabled = true) }
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
        var passwordError:String? = null
        if(currentState.password != null){
            passwordError = NewPasswordValidator.validate(currentState.password)
            onConfirmPasswordChange(currentState.confirmPassword)
        }
        if(userNameError!=null || passwordError!=null || _uiState.value.errorConfirmPassword!=null){
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