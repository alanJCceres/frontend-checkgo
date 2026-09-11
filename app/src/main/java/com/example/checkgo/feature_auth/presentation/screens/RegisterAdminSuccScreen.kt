package com.example.checkgo.feature_auth.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomLoadingButton
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterAdminSuccViewModel
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterSuccUiEvent
import kotlinx.coroutines.delay

@Composable
fun RegisterAdminSuccScreen(
    navController: NavController,
    viewModel: RegisterAdminSuccViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var isErrorToast by remember {mutableStateOf(true)}

    LaunchedEffect(toastMessage) {
        if (toastMessage!=null){
            delay(4000)
            toastMessage=null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect{ event ->
            when(event){
                is RegisterSuccUiEvent.Navigate -> {
                    navController.navigate(event.route){
                        popUpTo("registerAdminSuccScreen"){inclusive=true}
                    }
                }
                is RegisterSuccUiEvent.ShowToast ->{
                    isErrorToast=true
                    toastMessage=event.message
                }
            }
        }
    }

    //CONTENDOR UI
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp,50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), //equivalente al * en MAUI
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ){
//                Image(
//                    painter = painterResource(R.drawable.success),
//                    contentDescription = "Logo imagen",
//                    modifier = Modifier
//                        .width(200.dp)
//                        .height(200.dp),
//
//                    contentScale = ContentScale.Crop,
//                )
//            }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text="Registro exitoso !",style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text="Tu cuenta ha sido",color = Color.Gray)
                    Text(text="registrada correctamente",color = Color.Gray)
                }
            }
            CustomLoadingButton(
                isLoading = uiState.isLoading,
                normalText = "Continuar",
                loadingText = "Cargando...",
                onClick = { viewModel.onContinueClicked() }
            )
        }

        AnimatedVisibility(
            visible = toastMessage != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            toastMessage?.let { message ->
                CustomTopToast(message = message, isError = isErrorToast)
            }
        }
    }
}