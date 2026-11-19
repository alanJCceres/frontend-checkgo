package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import com.example.checkgo.core.ui.components.Step1LocationContentBottom
import com.google.maps.android.compose.ComposeMapColorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

private val DefaultLatLng = LatLng(-16.29, -63.59)
private const val DefaultZoom = 5f
private const val UserZoom = 17f

private fun Step1State.toLatLngOrNull(): LatLng? {
    val lat = latitud.toDoubleOrNull() ?: return null
    val lng = longitud.toDoubleOrNull() ?: return null
    return LatLng(lat, lng)
}
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Step1RegLocationScreen(
    state: Step1State,
    onChange: (Step1State) -> Unit,
    onNext: () -> Unit,
    onMapPositionSelected: (lat: Double, lng: Double) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var sheetVisible by remember { mutableStateOf(true) }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    val initialLatLng = remember { state.toLatLngOrNull() }
    var centeredOnUser by rememberSaveable { mutableStateOf(initialLatLng != null) }
    val currentOnMapPositionSelected by rememberUpdatedState(onMapPositionSelected)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            initialLatLng ?: DefaultLatLng,
            if (initialLatLng != null) UserZoom else DefaultZoom
        )
    }
    // Verificamos permisos de la camara
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    val hasLocationPermission = permissionsState.permissions.any { it.status.isGranted }
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


    LaunchedEffect(Unit) {
        if (!permissionRequested && !hasLocationPermission) {
            permissionRequested = true
            permissionsState.launchMultiplePermissionRequest()
        }
    }


    @SuppressLint("MissingPermission")
    fun centerOnUserOnce() {
        LocationServices.getFusedLocationProviderClient(context)
            .getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            )
            .addOnSuccessListener { location ->
                if (location != null && !centeredOnUser) { // location es null si el GPS está apagado: no pasa nada, el mapa sigue usable
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

    // Guarda automaticamente el centro cuando se detiene la camara
    LaunchedEffect(cameraPositionState,) {
        snapshotFlow { cameraPositionState.isMoving }
            .filter { moving ->
                !moving && cameraPositionState.cameraMoveStartedReason !=
                        CameraMoveStartedReason.NO_MOVEMENT_YET
            }
            .collect {
                val target = cameraPositionState.position.target
                currentOnMapPositionSelected(target.latitude, target.longitude)
            }
    }

    //Esconde y muestra el sheet
    LaunchedEffect(cameraPositionState) {
        snapshotFlow { cameraPositionState.isMoving }
            .collectLatest { moving ->
                if (moving) {
                    sheetVisible = false
                } else {
                    delay(250)
                    sheetVisible = true
                }
            }
    }

    //INTERFAZ
    Box(modifier = Modifier.fillMaxSize()){

        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings,
            mapColorScheme = ComposeMapColorScheme.LIGHT
        )

        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Punto seleccionado",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .align(Alignment.Center)
                .size(48.dp)
                .offset(y = (-24).dp)
        )
        //BotomSheet
        AnimatedVisibility (
            visible = sheetVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically (initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically (targetOffsetY = { it }) + fadeOut()
        ) {
            Surface(
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Step1LocationContentBottom(
                    nameLocation = state.nombreLocation,
                    onNameLocationChange = { onChange(state.copy(nombreLocation = it, nombreLocationError = null)) },
                    isErrorNameLocation = state.nombreLocationError != null,
                    errorMessageNameLocation = state.nombreLocationError,
                    address = state.address,
                    onNextClick = onNext,
                    buttonEnabled = state.latitud.isNotBlank() && state.longitud.isNotBlank(),
                )
            }
        }
    }
}