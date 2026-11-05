package com.example.checkgo.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step1LocationBottomSheet(
    nameLocation: String,
    onNameLocationChange: (String) -> Unit,
    isErrorNameLocation: Boolean,
    errorMessageNameLocation: String?,
    address: String,
    onAddressChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onNextClick: () -> Unit,
    buttonEnabled: Boolean=true,
    latitudLogitud: String
) {
    val modoOscuro = isSystemInDarkTheme()
    val circleBackground = Color(0xFFEEEEEE)
    val scope = rememberCoroutineScope()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        scrimColor = Color.Transparent,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value=nameLocation,
                onValueChange = {onNameLocationChange(it)},
                onBlur = {},
                label = "Nombre de la ubicación",
                placeholder = "Nombre de la ubicación",
                isError = isErrorNameLocation,
                errorMessage = errorMessageNameLocation,
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(text = latitudLogitud,style = MaterialTheme.typography.bodyMedium)
            CustomTextField(
                value=address,
                onValueChange = {onAddressChange(it)},
                onBlur = {},
                label = "Dirección seleccionada",
                placeholder = "Dirección seleccionada",
                enabled = false,
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(26.dp))
            //BOTON PARA EDITAR
            CustomButton(
                text="Siguiente",
                enabled = buttonEnabled,
                onClick = {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion {
                            // se ejecuta cuando la animación de cierre terminó
                            onDismissRequest()   // limpia el estado en el ViewModel
                            onNextClick()        // navega
                        }
                }
            )
            Spacer(modifier = Modifier.height(26.dp))
        }
    }
}