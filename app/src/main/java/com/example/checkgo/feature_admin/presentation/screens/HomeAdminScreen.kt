package com.example.checkgo.feature_admin.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.RegisterLocationGraph
import com.example.checkgo.feature_admin.presentation.viewmodel.HomeAdminViewModel

@Composable
fun HomeAdminScreen(
navController: NavController,
viewModel: HomeAdminViewModel = hiltViewModel()
) {
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
            Text(text = "HOME ADMIN")
            CustomButton(
                isPrimary = false,
                text="Agregar usuario",
                onClick = {navController.navigate("registerUser")}
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomButton(
                isPrimary = false,
                text="Agregar ubicación",
                onClick = {navController.navigate(RegisterLocationGraph)}
            )
            Spacer(modifier = Modifier.height(16.dp))
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
    }

}