package com.example.checkgo.feature_auth.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.feature_auth.data.dto.LoginUserRequestDto
import com.example.checkgo.feature_auth.domain.usecase.LoginUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterSuccUiState(
    val isLoading: Boolean=false,
)
sealed class RegisterSuccUiEvent{
    data class Navigate(val route: String): RegisterSuccUiEvent()
    data class ShowToast(val message: String): RegisterSuccUiEvent()
}
@HiltViewModel
class RegisterAdminSuccViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle, //captura los parametros que se envian desde la pantalla anterior
    private val loginUserUseCase: LoginUserUseCase
): ViewModel() {
    private val userName: String = checkNotNull(savedStateHandle["userName"])
    private val password: String = checkNotNull(savedStateHandle["password"])
    private val _uiState = MutableStateFlow(RegisterSuccUiState())
    val uiState: StateFlow<RegisterSuccUiState> = _uiState.asStateFlow()
    private val _navigationEvent = Channel<RegisterSuccUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onContinueClicked(){
        _uiState.update { it.copy(isLoading = true) }
        val login= LoginUserRequestDto(
            userName = userName,
            password = password
        )
        viewModelScope.launch {
            loginUserUseCase(login).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.send(RegisterSuccUiEvent.Navigate("homeAdmin"))
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