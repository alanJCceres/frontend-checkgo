package com.example.checkgo.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextBody
import com.example.checkgo.core.ui.theme.StyleTextTituloBody

@Composable
fun Step1LocationContentBottom(
    nameLocation: String,
    onNameLocationChange: (String) -> Unit,
    isErrorNameLocation: Boolean,
    errorMessageNameLocation: String?,
    address: String = "Mueva el mapa para encontrar una ubicación",
    onNextClick: () -> Unit,
    buttonEnabled: Boolean=true,
) {
    val modoOscuro = isSystemInDarkTheme()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Escoge tu punto de llegada",
                style = StyleTextTituloBody,
                color = MaterialTheme.colorScheme.secondary,
            )
            CustomTextField(
                value=nameLocation,
                onValueChange = {onNameLocationChange(it)},
                onBlur = {},
                label = "Nombre de la ubicación",
                placeholder = "Sucursal 1",
                isError = isErrorNameLocation,
                errorMessage = errorMessageNameLocation,
                leadingIcon = Icons.Default.Map
            )
            Column(
                modifier = Modifier

            ) {
                Text(
                    text = "Ubicación seleccionada",
                    style = StyleTextBody,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Row(modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Punto seleccionado",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = address,
                        style = StyleTextBody,
                        color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                    )
                }

            }

            CustomButton(
                text="Siguiente",
                enabled = buttonEnabled,
                onClick = onNextClick
            )
        }
}