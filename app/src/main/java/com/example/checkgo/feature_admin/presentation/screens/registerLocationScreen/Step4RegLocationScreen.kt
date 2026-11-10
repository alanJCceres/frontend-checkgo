package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_admin.presentation.viewmodel.Step4State


@Composable
fun Step4RegLocationScreen(
    state: Step4State,
    onChange: (Step4State) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
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
                text = "Guardar",
                onClick = onNext,
                modifier = Modifier.weight(1f)
            )
        }
    }
}