package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.bus.UserEvent
import com.example.checkgo.core.bus.UserEventBus
import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationDayRequestDto
import com.example.checkgo.feature_admin.data.repository.GeocoderRepository
import com.example.checkgo.feature_admin.domain.usecase.UsersUseCase
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
import org.mobilenativefoundation.store.store5.StoreReadResponse
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
    val usersSelected: List<String> = emptyList(),
    val usersError: String? = null,
)

data class RegistroUiState(
    val step1: Step1State = Step1State(),
    val step2: Step2State = Step2State(),
    val step3: Step3State = Step3State(),
    val step4: Step4State = Step4State(),

    val isSubmitting: Boolean = false,
    val submitError: String? = null
)

sealed interface Step4UsersUiState {
    data object Loading : Step4UsersUiState
    data class Success(val users: List<UserResponseDto>) : Step4UsersUiState
    data class Error(val message: String) : Step4UsersUiState
    data object Empty : Step4UsersUiState
}

sealed interface SaveState {
    data object Idle : SaveState
    data object Saving : SaveState
    data object Success : SaveState
    data class Error(val message: String) : SaveState
}

@HiltViewModel
class RegisterLocationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val geocoderRepository: GeocoderRepository,
    private val usersUseCase: UsersUseCase,
    private val eventBus: UserEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState = _uiState.asStateFlow()
    private val _usersState = MutableStateFlow<Step4UsersUiState>(Step4UsersUiState.Loading)
    val usersState = _usersState.asStateFlow()
    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()
    private var usersJob: Job? = null
    private var geocodeJob: Job? = null

    init {
        viewModelScope.launch {
            eventBus.events.collect { event ->
                when (event) {
                    is UserEvent.UserCreated -> loadUsers(force = true, silent = true)
                }
            }
        }
    }
    fun loadUsers(force: Boolean = false, silent: Boolean = false) {

        if (!force && _usersState.value is Step4UsersUiState.Success) return
        if (!force && usersJob?.isActive == true) return

        usersJob?.cancel()   // un refresco forzado reemplaza a la carga anterior, así no se pierde el evento
        val previous = _usersState.value as? Step4UsersUiState.Success
        val showSpinner = !(silent && previous != null)
        if (showSpinner) _usersState.value = Step4UsersUiState.Loading

        usersJob = viewModelScope.launch {
            usersUseCase(force).collect { response ->
                when (response) {
                    is StoreReadResponse.Loading -> {
                        if (_usersState.value !is Step4UsersUiState.Success) {
                            _usersState.value = Step4UsersUiState.Loading
                        }
                    }
                    is StoreReadResponse.Data -> {
                        val users = response.value
                        _usersState.value = if (users.isEmpty()) {
                            Step4UsersUiState.Empty
                        } else {
                            Step4UsersUiState.Success(users)
                        }
                    }
                    is StoreReadResponse.Error -> {
                        // refresco silencioso: si falla, se conserva la lista que ya se veía
                        if (!showSpinner) return@collect
                        val errorMsg = response.errorMessageOrNull() ?: "Error desconocido"
                        _usersState.value = Step4UsersUiState.Error(errorMsg)
                    }
                    else -> {}
                }
            }
        }
    }
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
    //onchange del step3
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
    //onchange del step4
    fun onUserSelectionChange(userId: String, selected: Boolean) {
        _uiState.update { cur ->
            val current = cur.step4.usersSelected
            val updated = if (selected) (current + userId).distinct() else current - userId
            cur.copy(step4 = cur.step4.copy(usersSelected = updated, usersError = null))
        }
    }


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
        val error = if (_uiState.value.step4.usersSelected.isEmpty())
            "Selecciona al menos un usuario" else null
        _uiState.update { it.copy(step4 = it.step4.copy(usersError = error)) }
        return error == null
    }
    fun save() {
        if (_saveState.value is SaveState.Saving) return // evita doble toque
        if (!validateStep4()) return
        val request = null //aqui añadir el request
        _saveState.value = SaveState.Saving
        viewModelScope.launch {
            // TODO: aquí va tu llamada, por ejemplo:
            // createLocationUseCase(request)
            //     .onSuccess { _saveState.value = SaveState.Success }
            //     .onFailure { _saveState.value = SaveState.Error(it.message ?: "No se pudo guardar") }
        }
    }

    //funcion auxiliar para el mapa
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