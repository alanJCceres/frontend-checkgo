package com.example.checkgo.feature_auth.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import com.example.checkgo.feature_auth.domain.usecase.LoginUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterSuccUiState(
    val isLoading: Boolean=false,
)
sealed class RegisterSuccUiEvent{
    data class Navigate(val route: String): RegisterSuccUiEvent()
    data class ShowToast(val message: String): RegisterSuccUiEvent()
}
class RegisterAdminSuccViewModel: ViewModel() {
   private val loginUserUseCase = LoginUserUseCase()
    private val _uiState = MutableStateFlow(RegisterSuccUiState())
    val uiState: StateFlow<RegisterSuccUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<RegisterSuccUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onContinueClicked(){
        _uiState.update { it.copy(isLoading = true) }
        val login= LoginUserRequestDto(
            userName = "alan",
            password = "12345678"
        )
        viewModelScope.launch {
            loginUserUseCase(login).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.send(RegisterSuccUiEvent.Navigate("HomeAdmin"))
                },
                onFailure = {exception ->
                    _uiState.update { it.copy(isLoading = false) }
                    val errorMessage = exception.message?:"Error desconocido"
                    _navigationEvent.send(RegisterSuccUiEvent.ShowToast(errorMessage))
                }
            )
        }
    }
}