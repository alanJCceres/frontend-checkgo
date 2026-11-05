package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.checkgo.feature_admin.presentation.viewmodel.Step1State
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.LatLng
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.compose.runtime.setValue
import com.example.checkgo.core.ui.components.SecurityBottomSheet
import com.example.checkgo.core.ui.components.Step1LocationBottomSheet

private val DefaultLatLng = LatLng(-16.29, -63.59)
private const val DefaultZoom = 5f
private const val UserZoom = 17f

private fun Step1State.toLatLngOrNull(): LatLng? {
    val lat = latitud.toDoubleOrNull() ?: return null
    val lng = longitud.toDoubleOrNull() ?: return null
    return LatLng(lat, lng)
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Step1RegLocationScreen(
    state: Step1State,
    onChange: (Step1State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Evita capturas obsoletas dentro de los efectos de larga vida
    val currentState by rememberUpdatedState(state)
    val currentOnChange by rememberUpdatedState(onChange)

    // Si ya había coordenadas (volver desde el step 2), arrancamos ahí
    val initialLatLng = remember { state.toLatLngOrNull() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            initialLatLng ?: DefaultLatLng,
            if (initialLatLng != null) UserZoom else DefaultZoom
        )
    }

    // ---- Permisos (opcionales: el mapa funciona sin ellos) ----
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val hasLocationPermission = permissionsState.permissions.any { it.status.isGranted }

    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (!permissionRequested && !hasLocationPermission) {
            permissionRequested = true
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    // ---- Centrar una sola vez en la ubicación del usuario ----
    var centeredOnUser by rememberSaveable { mutableStateOf(initialLatLng != null) }

    @SuppressLint("MissingPermission")
    fun centerOnUserOnce() {
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )
            .addOnSuccessListener { location ->
                // location es null si el GPS está apagado: no pasa nada, el mapa sigue usable
                if (location != null && !centeredOnUser) {
                    centeredOnUser = true
                    scope.launch {
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(location.latitude, location.longitude),
                                UserZoom
                            )
                        )
                    }
                }
            }
    }

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && !centeredOnUser) centerOnUserOnce()
    }

    // ---- Cuando la cámara se detiene tras un movimiento, guardamos el centro ----
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .filter { moving ->
                !moving && cameraPositionState.cameraMoveStartedReason !=
                        CameraMoveStartedReason.NO_MOVEMENT_YET
            }
            .collect {
                val target = cameraPositionState.position.target
                currentOnChange(
                    currentState.copy(
                        latitud = String.format(Locale.US, "%.6f", target.latitude),
                        longitud = String.format(Locale.US, "%.6f", target.longitude)
                    )
                )
            }
    }

    val mapProperties = remember(hasLocationPermission) {
        MapProperties(isMyLocationEnabled = hasLocationPermission)
    }
    val mapUiSettings = remember(hasLocationPermission) {
        MapUiSettings(
            myLocationButtonEnabled = hasLocationPermission,
            zoomControlsEnabled = false,
            mapToolbarEnabled = false
        )
    }

    //DESDE AQUI MODIFICAR
    Box(
        modifier = Modifier

            .fillMaxSize()
    ){
            GoogleMap(
                modifier = Modifier.matchParentSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings
            )

            // Pin fijo al centro: la punta queda exactamente en el centro del mapa
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Punto seleccionado",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .offset(y = (-24).dp)
            )
    }
    Step1LocationBottomSheet(
        nameLocation = state.nombreLocation,
        onNameLocationChange = { onChange(state.copy(nombreLocation = it, nombreLocationError = null)) },
        isErrorNameLocation = state.nombreLocationError != null,
        errorMessageNameLocation = state.nombreLocationError,
        address = state.address,
        onAddressChange = { onChange(state.copy(address = it)) },
        onDismissRequest = {},
        onNextClick = onNext,
        buttonEnabled = state.latitud.isNotBlank() && state.longitud.isNotBlank(),
        latitudLogitud = if (state.latitud.isNotBlank() && state.longitud.isNotBlank())
            "Lat: ${state.latitud}  |  Lng: ${state.longitud}"
        else
            "Mueve el mapa para seleccionar un punto"
    )
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        OutlinedTextField(
//            value = state.nombreLocation,
//            onValueChange = { onChange(state.copy(nombreLocation = it, nombreLocationError = null)) },
//            label = { Text("Nombre de la ubicación") },
//            isError = state.nombreLocationError != null,
//            supportingText = state.nombreLocationError?.let { { Text(it) } },
//            singleLine = true,
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
//            GoogleMap(
//                modifier = Modifier.matchParentSize(),
//                cameraPositionState = cameraPositionState,
//                properties = mapProperties,
//                uiSettings = mapUiSettings
//            )
//
//            // Pin fijo al centro: la punta queda exactamente en el centro del mapa
//            Icon(
//                imageVector = Icons.Default.LocationOn,
//                contentDescription = "Punto seleccionado",
//                tint = MaterialTheme.colorScheme.error,
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .size(48.dp)
//                    .offset(y = (-24).dp)
//            )
//        }
//
//        Text(
//            text = if (state.latitud.isNotBlank() && state.longitud.isNotBlank())
//                "Lat: ${state.latitud}  |  Lng: ${state.longitud}"
//            else
//                "Mueve el mapa para seleccionar un punto",
//            style = MaterialTheme.typography.bodyMedium
//        )
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Atrás") }
//            Button(
//                onClick = onNext,
//                enabled = state.latitud.isNotBlank() && state.longitud.isNotBlank(),
//                modifier = Modifier.weight(1f)
//            ) { Text("Siguiente") }
//        }
//    }
}