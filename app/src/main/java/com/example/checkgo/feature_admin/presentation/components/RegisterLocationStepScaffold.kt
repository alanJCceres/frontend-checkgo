package com.example.checkgo.feature_admin.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RegisterLocationStepScaffold(
    currentStep: Int,
    previousStep: Int,
    onStepShown: (Int) -> Unit,
    modifier: Modifier = Modifier,
    overlay: Boolean = false,
    onStepClick: ((Int) -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (overlay) {
        Box(modifier.fillMaxSize()) {
            content()
            RegisterStepper(
                steps = RegisterLocationSteps,
                currentStep = currentStep,
                previousStep = previousStep,
                onStepShown = onStepShown,
                onStepClick = onStepClick,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
    } else {
        Column(modifier.fillMaxSize()) {
            RegisterStepper(
                steps = RegisterLocationSteps,
                currentStep = currentStep,
                previousStep = previousStep,
                onStepShown = onStepShown,
                onStepClick = onStepClick
            )
            Box(Modifier.weight(1f).fillMaxWidth()) { content() }
        }
    }
}