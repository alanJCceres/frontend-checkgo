package com.example.checkgo.feature_user.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_user.presentation.viewmodel.HomeUserViewModel
import androidx.compose.runtime.*
import com.example.checkgo.core.ui.components.SecurityBottomSheet

@Composable
fun HomeUserScreen(
    navController: NavController,
    viewModel: HomeUserViewModel = hiltViewModel()
) {
    var showSecuritySheet by remember { mutableStateOf(false) }

    // Simulamos la verificación al cargar la pantalla
    LaunchedEffect(Unit) {
        // Aquí iría tu lógica de verificación real (ej: viewModel.checkRegistration())
        // Para este ejemplo, simulamos que detecta que necesita mostrarse.
        val needsRegistration = true // Cambia esto según tu lógica

        if (needsRegistration) {
            showSecuritySheet = true
        }
    }
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp, 50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "HOLA MUNDO DESDE HOME USER")
        }
        CustomButton(
            text="cerrar sesion",
            onClick = {
                viewModel.logout {
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            }
        )
    }
    if (showSecuritySheet) {
        SecurityBottomSheet(
            onDismissRequest = {
                // Lógica al presionar el botón cerrar o al tocar fuera del sheet
                showSecuritySheet = false
                // Aquí podrías avisar a tu ViewModel que el usuario ya leyó el aviso
                // viewModel.onSecurityInfoDismissed()
            }
        )
    }
}