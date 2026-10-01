package com.example.checkgo.feature_user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.common.DeviceInfoProvider
import com.example.checkgo.core.data.dto.DeviceRequestDto
import com.example.checkgo.core.data.localStorage.AppDataStoreManager
import com.example.checkgo.core.data.localStorage.TokenManager
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterUiEvent
import com.example.checkgo.feature_user.domain.usecase.RegisterDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiEvent{
    //data class Navigate(val route: String): HomeUiEvent()
    data class ShowErrorToast(val message: String): HomeUiEvent()
}
@HiltViewModel
class HomeUserViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val appDataStoreManager: AppDataStoreManager,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val deviceInfoProvider: DeviceInfoProvider

): ViewModel(){
    private val _showSecuritySheet = MutableStateFlow(false)
    val showSecuritySheet: StateFlow<Boolean> = _showSecuritySheet.asStateFlow()
    private val _navigationEvent = Channel<HomeUiEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        checkAndRegisterDeviceAutomatically()
    }

    private fun checkAndRegisterDeviceAutomatically() {
        viewModelScope.launch {

            val needsRegistration = appDataStoreManager.getNeedsRegistration()
            if (needsRegistration) {
                val getAndroidId = deviceInfoProvider.getAndroidId()
                val deviceRequest = DeviceRequestDto(
                    androidId=getAndroidId
                )
                registerDeviceUseCase(deviceRequest).fold(
                    onSuccess = {
                        appDataStoreManager.saveNeedsRegistration(false)
                        _showSecuritySheet.value = true
                    },
                    onFailure = { exception ->
                        val errorMessage = exception.message?:"Error desconocido"
                        _navigationEvent.send(HomeUiEvent.ShowErrorToast(errorMessage))
                    }
                )
            }
        }
    }
    fun dismissSecuritySheet() {
        _showSecuritySheet.value = false
    }
    fun logout(onLogoutComplete: () -> Unit) {
        tokenManager.clearAll()
        onLogoutComplete()
    }
}