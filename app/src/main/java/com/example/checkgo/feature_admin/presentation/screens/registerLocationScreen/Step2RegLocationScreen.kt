package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Step2State(
    val descripcion: String = "",
    val descripcionError: String? = null,
    // ...
)
@Composable
fun Step2RegLocationScreen(
    state: Step2State,
    onChange: (Step2State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Row {
            TextButton(onClick = onBack) { Text("Atrás") }
            Button(onClick = onNext) { Text("Registrar") }
        }
    }
}