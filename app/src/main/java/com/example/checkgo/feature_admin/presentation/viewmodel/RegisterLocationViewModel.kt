package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.feature_admin.data.repository.GeocoderRepository
import com.example.checkgo.feature_admin.domain.validators.NewLocationNameValidator
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.Step2State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

data class Step1State(
    val nombreLocation: String = "",
    val address: String = "",
    val latitud: String = "",
    val longitud: String = "",

    val nombreLocationError: String? = null,
)

data class RegistroUiState(
    val step1: Step1State = Step1State(),
    val step2: Step2State = Step2State(),
    val isSubmitting: Boolean = false,
    val submitError: String? = null
)

sealed interface RegistroEvent {
    data object Success : RegistroEvent
    data class ShowMessage(val message: String) : RegistroEvent
}

@HiltViewModel
class RegisterLocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val geocoderRepository: GeocoderRepository
): ViewModel() {
    private var geocodeJob: Job? = null
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<RegistroEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onStep1Change(new: Step1State) = _uiState.update { it.copy(step1 = new) }
    fun onStep2Change(new: Step2State) = _uiState.update { it.copy(step2 = new) }


    fun validateStep1(): Boolean {
        val estadoActualStep1 = _uiState.value.step1
        val locationNamevalidationError = NewLocationNameValidator.validate(estadoActualStep1.nombreLocation)
        _uiState.update { it.copy(
            step1 = estadoActualStep1.copy(nombreLocationError = locationNamevalidationError)
        ) }
        return locationNamevalidationError == null;
    }
    fun validateStep2(): Boolean {
        return true;
    }
    fun onMapPositionSelected(lat: Double, lng: Double) {
        _uiState.update {
            it.copy(
                step1 = it.step1.copy(
                    latitud = String.format(Locale.US, "%.6f", lat),
                    longitud = String.format(Locale.US, "%.6f", lng)
                )
            )
        }

        geocodeJob?.cancel() // si el usuario vuelve a mover el mapa, descarta la búsqueda anterior
        geocodeJob = viewModelScope.launch {
            val address = geocoderRepository.getAddress(lat, lng) ?: return@launch
            _uiState.update { it.copy(step1 = it.step1.copy(address = address)) }
        }
    }
}