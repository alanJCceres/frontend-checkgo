package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.checkgo.feature_admin.presentation.viewmodel.Step2State
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlin.math.roundToInt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.ui.components.BadgeDefaults
import com.example.checkgo.core.ui.components.CustomBadge
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.components.CustomTextField
import com.example.checkgo.core.ui.components.SelectableOptionCard
import com.example.checkgo.core.ui.theme.StyleTextBody
import com.example.checkgo.core.ui.theme.StyleTextBodySmall
import com.example.checkgo.core.ui.theme.StyleTextTituloBody
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlin.math.cos

private const val MinRadius = 20
private const val MaxRadius = 500
@Composable
fun Step2RegLocationScreen(
    center: LatLng,
    state: Step2State,
    onChange: (Step2State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val radio = state.radio.coerceIn(MinRadius, MaxRadius)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(center, 15f)
    }
    var mapLoaded by remember { mutableStateOf(false) }
    val paddingPx = with(LocalDensity.current) { 32.dp.toPx().roundToInt() }
    val primary = MaterialTheme.colorScheme.primary

    // Reencuadra la cámara para que el círculo completo quepa siempre en el mapa
    LaunchedEffect(radio, mapLoaded) {
        if (!mapLoaded) return@LaunchedEffect
        cameraPositionState.move(
            CameraUpdateFactory.newLatLngBounds(
                boundsForRadius(center, radio.toDouble()),
                paddingPx
            )
        )
    }

    // Mapa de solo lectura: sin gestos, para no interferir con el scroll de la pantalla
    val uiSettings = remember {
        MapUiSettings(
            scrollGesturesEnabled = false,
            zoomGesturesEnabled = false,
            rotationGesturesEnabled = false,
            tiltGesturesEnabled = false,
            zoomControlsEnabled = false,
            compassEnabled = false,
            mapToolbarEnabled = false,
            myLocationButtonEnabled = false
        )
    }



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp,20.dp),
    ) {
        // INTERFAZ
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //SECCION RADIO CON MAPA
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Radio de la ubicación",
                        style = StyleTextTituloBody
                    )
                    CustomBadge(
                        text = "$radio m",
                        colors = BadgeDefaults.Blue,
                        fontSize = 14,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    cameraPositionState = cameraPositionState,
                    uiSettings = uiSettings,
                    mapColorScheme = ComposeMapColorScheme.LIGHT,
                    onMapLoaded = { mapLoaded = true }
                ) {
                    Marker(state = rememberMarkerState(position = center))
                    Circle(
                        center = center,
                        radius = radio.toDouble(),   // en metros
                        fillColor = primary.copy(alpha = 0.2f),
                        strokeColor = primary,
                        strokeWidth = 4f
                    )
                }
                //slider
                Column() {
                    Slider(
                        value = radio.toFloat(),
                        onValueChange = { onChange(state.copy(radio = it.roundToInt())) },
                        valueRange = MinRadius.toFloat()..MaxRadius.toFloat(),
                        colors = SliderDefaults.colors(
                            inactiveTrackColor = Color.LightGray,
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$MinRadius m", style = MaterialTheme.typography.labelSmall)
                        Text("$MaxRadius m", style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Define el área circular donde los usuarios podrán marcar su asistencia.",
                        style = StyleTextBodySmall,
                        color = Color.Gray
                    )
                }
            }

            // SECCION TIPO DE REGISTRO
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tipo de registro",
                    style = StyleTextTituloBody
                )
                SelectableOptionCard(
                    icon = Icons.Default.SwapHoriz,
                    title = "Entrada y salida",
                    description = "Se controla cuando la persona llega y cuando se retira.",
                    selected = state.arrivalAndDeparture,
                    onClick = {
                        onChange(
                            state.copy(
                                arrivalAndDeparture = true,
                                arrivalOnly = false,
                                exitOnly = false
                            )
                        )
                    }
                )
                SelectableOptionCard(
                    icon = Icons.AutoMirrored.Filled.Login,
                    title = "Solo entrada",
                    description = "Solo se controla cuando la persona llega al lugar.",
                    selected = state.arrivalOnly,
                    onClick = {
                        onChange(
                            state.copy(
                                arrivalOnly = true,
                                arrivalAndDeparture = false,
                                exitOnly = false
                            )
                        )
                    }
                )
                SelectableOptionCard(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Solo salida",
                    description = "Solo se controla cuando la persona se retira del lugar.",
                    selected = state.exitOnly,
                    onClick = {
                        onChange(
                            state.copy(
                                exitOnly = true,
                                arrivalOnly = false,
                                arrivalAndDeparture = false
                            )
                        )
                    }
                )

            }

            //SECCION TOLERANCIA
            Column(
                modifier = Modifier
                    .padding(0.dp,10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tolerancia (Minutos)",
                    style = StyleTextTituloBody
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween

                ) {
                    CustomTextField(
                        value = state.tolerance.toString(),
                        onValueChange = { input ->
                            val digits = input.filter { it.isDigit() }.take(3)
                            onChange(state.copy(tolerance = digits.toIntOrNull() ?: 0, toleranceError = null))
                        },
                        label = "Minutos",
                        isError = state.toleranceError != null,
                        modifier = Modifier.width(110.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "Minutos permitidos despues de la hora programada sin marcar retraso.",
                        style = StyleTextBody,
                        color = Color.Gray,
                        modifier = Modifier.weight(2f)
                    )
                }
                state.toleranceError?.let {
                    Text(text = it,
                        color = Color.Red,
                        fontSize = 14.sp,)
                }
            }

        }

        //BOTONES
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButton(
                text = "Atrás",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                isPrimary = false
            )
            CustomButton(
                text = "Siguiente",
                onClick = onNext,
                modifier = Modifier.weight(1f)
            )
        }
    }

}
private fun boundsForRadius(center: LatLng, radiusMeters: Double): LatLngBounds {
    val metersPerDegreeLat = 111_320.0
    val metersPerDegreeLng = metersPerDegreeLat *
            cos(Math.toRadians(center.latitude)).coerceAtLeast(0.01)
    val dLat = radiusMeters / metersPerDegreeLat
    val dLng = radiusMeters / metersPerDegreeLng
    return LatLngBounds(
        LatLng(center.latitude - dLat, center.longitude - dLng),
        LatLng(center.latitude + dLat, center.longitude + dLng)
    )
}