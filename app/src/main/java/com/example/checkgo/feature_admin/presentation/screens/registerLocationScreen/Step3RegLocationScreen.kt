package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.runtime.Composable

data class Step3State(
    val descripcion: String = "",
    val descripcionError: String? = null,
    // ...
)

@Composable
fun Step3RegLocationScreen(
    state: Step3State,
    onChange: (Step3State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
}