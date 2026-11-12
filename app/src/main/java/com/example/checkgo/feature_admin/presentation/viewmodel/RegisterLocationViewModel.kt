package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationDayRequestDto
import com.example.checkgo.feature_admin.data.repository.GeocoderRepository
import com.example.checkgo.feature_admin.domain.validators.NewLocationNameValidator
import com.example.checkgo.feature_admin.domain.validators.NewToleranceValidator
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.MAX_INTERVALS_PER_DAY
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.defaultWeek
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.isComplete
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.normalizedFor
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.toScheduleMode
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
data class Step2State(
    val arrivalAndDeparture: Boolean = true,
    val arrivalOnly: Boolean = false,
    val exitOnly: Boolean = false,
    val tolerance: Int=0,
    val radio: Int=20,

    val toleranceError: String? = null,
)
data class Step3State(
    val locationSchedules: List<ArrivalLocationDayRequestDto>?=null,
    val scheduleError: String? = null,
)
data class Step4State(
    val descripcion: String = "",
    val descripcionError: String? = null,
    // ...
)

data class RegistroUiState(
    val step1: Step1State = Step1State(),
    val step2: Step2State = Step2State(),
    val step3: Step3State = Step3State(),
    val step4: Step4State = Step4State(),

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
    fun onStep2Change(new: Step2State){
        _uiState.update { cur ->
            val oldMode = cur.step2.toScheduleMode()
            val newMode = new.toScheduleMode()
            cur.copy(
                step2 = new,
                step3 = if (oldMode != newMode)
                    cur.step3.copy(locationSchedules = cur.step3.locationSchedules?.normalizedFor(newMode))
                else cur.step3
            )
        }
    }
    fun onScheduleDayChange(new: ArrivalLocationDayRequestDto){
        _uiState.update { cur ->
            val currentWeek = cur.step3.locationSchedules ?: defaultWeek()
            cur.copy(
                step3 = cur.step3.copy(
                    locationSchedules = currentWeek.map { if (it.day == new.day) new else it },
                    scheduleError = null
                )
            )
        }
    }
    fun onStep4Change(new: Step4State) = _uiState.update { it.copy(step4 = new) }



    fun validateStep1(): Boolean {
        val estadoActualStep1 = _uiState.value.step1
        val locationNamevalidationError = NewLocationNameValidator.validate(estadoActualStep1.nombreLocation)
        _uiState.update { it.copy(
            step1 = estadoActualStep1.copy(nombreLocationError = locationNamevalidationError)
        ) }
        return locationNamevalidationError == null;
    }
    fun validateStep2(): Boolean {
        val estadoActualStep2 = _uiState.value.step2
        val toleranceValidationError = NewToleranceValidator.validate(estadoActualStep2.tolerance)
        _uiState.update { it.copy(
            step2 = estadoActualStep2.copy(toleranceError = toleranceValidationError)
        ) }
        return toleranceValidationError == null;
    }
    fun validateStep3(): Boolean {
        val mode = _uiState.value.step2.toScheduleMode()
        val enabledDays = (_uiState.value.step3.locationSchedules ?: defaultWeek()).filter { it.enabled }

        val error = when {
            enabledDays.isEmpty() -> "Activa al menos un día"
            enabledDays.any { it.intervalos.isEmpty() } -> "Cada día activo necesita al menos un horario"
            enabledDays.any { d -> d.intervalos.any { !it.isComplete(mode) } } -> "Completa todas las horas"
            enabledDays.any { it.intervalos.size > MAX_INTERVALS_PER_DAY } -> "Máximo $MAX_INTERVALS_PER_DAY horarios por día"
            enabledDays.any { it.intervalos.distinct().size != it.intervalos.size } -> "Hay horarios repetidos"
            else -> null
        }
        _uiState.update { it.copy(step3 = it.step3.copy(scheduleError = error)) }
        return error == null
    }
    fun validateStep4(): Boolean {
        return true;
    }
    fun submit() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
        }
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