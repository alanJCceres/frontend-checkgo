package com.example.checkgo.feature_admin.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CardUser
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloHeader
import com.example.checkgo.feature_admin.presentation.viewmodel.UsersViewModel
import androidx.compose.runtime.getValue
import com.example.checkgo.feature_admin.presentation.viewmodel.UserUiState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.components.EmptyStateView
import com.example.checkgo.core.ui.components.InfoUserBottomSheet

@Composable
fun UsersScreen(
    navController: NavController,
    viewModel: UsersViewModel= hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val selectedUser by viewModel.selectedUser.collectAsStateWithLifecycle()
    val modoOscuro = isSystemInDarkTheme()
    val toastMessage = viewModel.toastMessage

    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            kotlinx.coroutines.delay(4000)
            viewModel.dismissToast()
        }
    }

    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Usuarios",
                    style = StyleTextTituloHeader,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign= TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Gestión de usuarios de manera eficiente",
                    style = StyleTextSubHeader,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            CustomButton(
                isPrimary = false,
                text="Agregar usuario",
                onClick = {navController.navigate("registerUser")}
            )
            Spacer(modifier = Modifier.height(15.dp))
            Column(
            ) {
                when (val currentState = state) {
                    is UserUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }
                    is UserUiState.Empty -> {
                        EmptyStateView(
                            title = "Sin usuarios",
                            description = "Actualmente no hay usuarios registrados."
                        )
                    }
                    is UserUiState.ErrorScreen -> {

                        EmptyStateView(
                            title = "Error inesperado",
                            description = "Ocurrio un error inesperado al cargar los usuarios, posible causa: ${currentState.message}."
                        )
                    }
                    is UserUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
                        ) {
                            items(currentState.users) { user ->
                                CardUser(
                                    user = user,
                                    onOpenInfo = {viewModel.onShowInfoUser(user)}
                                )
                                Spacer(modifier = Modifier.height(15.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            toastMessage?.let { message ->
                CustomTopToast(message = message, isError = true)
            }
        }
    }

    selectedUser?.let { user ->
        InfoUserBottomSheet(
            user = user,
            onDismissRequest = { viewModel.dismissInfoUser() }
        )
    }
}