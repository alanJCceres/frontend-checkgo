package com.example.checkgo.feature_auth.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkgo.R
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.components.CustomLoadingButton
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloHeader
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterAdminSuccViewModel
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterSuccUiEvent
import kotlinx.coroutines.delay

@Composable
fun RegisterAdminSuccScreen(
    navController: NavController,
    viewModel: RegisterAdminSuccViewModel = hiltViewModel()
) {
    val modoOscuro = isSystemInDarkTheme()
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
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp,50.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f), //equivalente al * en MAUI
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(R.drawable.check_register),
                    contentDescription = "Logo imagen",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp),

                    contentScale = ContentScale.Crop,
                )
            }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text="¡Cuenta creada!",
                        style = StyleTextTituloHeader,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text="Tu usuario ha sido creado exitosamente.",
                        style = StyleTextSubHeader,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                    )
                    Text(
                        text="Ya puedes disfrutar de todos nuestros servicios y funcionalidades",
                        style = StyleTextSubHeader,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                    )
                }
            }
            CustomButton(
                text="Continuar",
                loadingText = "Cargando...",
                isLoading = uiState.isLoading,
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