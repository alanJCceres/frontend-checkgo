package com.example.checkgo.feature_user.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_user.presentation.viewmodel.HomeUserViewModel
import androidx.compose.runtime.*
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.components.SecurityBottomSheet
import com.example.checkgo.feature_user.presentation.viewmodel.HomeUiEvent
import kotlinx.coroutines.delay

@Composable
fun HomeUserScreen(
    navController: NavController,
    viewModel: HomeUserViewModel = hiltViewModel()
) {
    val showSecuritySheet by viewModel.showSecuritySheet.collectAsState()
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
                is HomeUiEvent.ShowErrorToast ->{
                    isErrorToast=true
                    toastMessage=event.message
                }
            }
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

    if (showSecuritySheet) {
        SecurityBottomSheet(
            onDismissRequest = {
                viewModel.dismissSecuritySheet()
            }
        )
    }
}