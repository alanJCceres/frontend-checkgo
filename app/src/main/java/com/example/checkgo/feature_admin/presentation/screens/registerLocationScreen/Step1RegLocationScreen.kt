package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Step1State(
    val nombre: String = "",
    val nombreError: String? = null,
    val email: String = "",
    val emailError: String? = null
)
@Composable
fun Step1RegLocationScreen(
    state: Step1State,
    onChange: (Step1State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = state.nombre,
            onValueChange = { onChange(state.copy(nombre = it, nombreError = null)) },
            label = { Text("Nombre") },
            isError = state.nombreError != null,
            supportingText = { state.nombreError?.let { Text(it) } }
        )
        OutlinedTextField(
            value = state.email,
            onValueChange = { onChange(state.copy(email = it, emailError = null)) },
            label = { Text("Email") },
            isError = state.emailError != null,
            supportingText = { state.emailError?.let { Text(it) } }
        )
        Row {
            TextButton(onClick = onBack) { Text("Atrás") }
            Button(onClick = onNext) { Text("Continuar") }
        }
    }
}