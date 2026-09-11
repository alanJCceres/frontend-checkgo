package com.example.checkgo.feature_auth.presentation.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.data.enum.UserRole
import com.example.checkgo.feature_auth.data.dto.RegisterUserRequestDto
import com.example.checkgo.feature_auth.domain.usecase.RegisterUserUseCase
import com.example.checkgo.feature_auth.domain.validators.EmailValidator
import com.example.checkgo.feature_auth.domain.validators.FullNameValidator
import com.example.checkgo.feature_auth.domain.validators.PasswordValidator
import com.example.checkgo.feature_auth.domain.validators.UserNameValidator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val fullname: String="",
    val email: String="",
    val userName: String="",
    val password: String="",
    val confirmPassword: String="",
    val isPasswordVisible: Boolean=false,
    val isConfirmPasswordVisible: Boolean=false,

    val errorFullName: String? = null,
    val errorEmail: String? = null,
    val errorUserName: String? = null,
    val errorPassword: String? = null,
    val errorConfirmPassword: String? = null,
    val isLoading: Boolean=false,
)
sealed class RegisterUiEvent{
    data class Navigate(val route: String): RegisterUiEvent()
    data class ShowToast(val message: String): RegisterUiEvent()
}
class RegisterViewModel: ViewModel() {
    private val registerUserUseCase = RegisterUserUseCase()
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<RegisterUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()
    // -- ESTADO GET (Reactivo) --
//    private val _currentUserId = MutableStateFlow("1")
//    val userQueryState: StateFlow<StoreReadResponse<User>> = _currentUserId
//        .flatMapLatest { id -> getUserUseCase(id) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5000),
//            initialValue = StoreReadResponse.Loading(origin = StoreReadResponse.Origin.Fetcher)
//        )



    fun onFullnameChange(newFullname:String){
        _uiState.update { estadoActual ->
            estadoActual.copy(
                fullname = newFullname,
                errorFullName = null
            )
        }
    }
    fun onEmailChange(newEmail:String){
        _uiState.update{estadoActual ->
            estadoActual.copy(
                email = newEmail,
                errorEmail = null
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
    fun onEmailFocusLost(){
        val estadoActual = _uiState.value
        val validationError = EmailValidator.validate(estadoActual.email)
        _uiState.update { it.copy(
            errorEmail = validationError
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
                email=currentState.email,
                userName = currentState.userName,
                password = currentState.password,
                rol = UserRole.SUPER_ADMIN,
                planPublicId = "a633b916-149e-4a0a-a527-799e4b7ba78f"
            )
            viewModelScope.launch {
                registerUserUseCase(userToSave).fold(
                    onSuccess = {
                        _uiState.update { it.copy(isLoading = false) }
                        _navigationEvent.send(RegisterUiEvent.Navigate("registerAdminSuccScreen"))
                    },
                    onFailure = {exception ->
                        _uiState.update { it.copy(isLoading = false) }
                        val errorMessage = exception.message?:"Error desconocido"
                        _navigationEvent.send(RegisterUiEvent.ShowToast(errorMessage))
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
        val emailError = EmailValidator.validate(currentState.email)
        if(fullnameError!=null || userNameError!=null ||
            passwordError!=null || emailError!=null || currentState.errorConfirmPassword!=null){
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