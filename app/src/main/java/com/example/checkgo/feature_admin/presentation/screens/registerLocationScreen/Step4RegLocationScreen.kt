package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.runtime.Composable

data class Step4State(
    val descripcion: String = "",
    val descripcionError: String? = null,
    // ...
)
@Composable
fun Step4RegLocationScreen(
    state: Step4State,
    onChange: (Step4State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
}