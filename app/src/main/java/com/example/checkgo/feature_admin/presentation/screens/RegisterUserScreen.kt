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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.components.CustomTextFieldClasic
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloHeader
import com.example.checkgo.feature_admin.presentation.viewmodel.RegisterUserUiEvent
import com.example.checkgo.feature_admin.presentation.viewmodel.RegisterUserViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterUserScreen(
    viewModel: RegisterUserViewModel = hiltViewModel()
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
                is RegisterUserUiEvent.ShowSuccessToast -> {
                    isErrorToast=false
                    toastMessage=event.message
                }
                is RegisterUserUiEvent.ShowErrorToast ->{
                    isErrorToast=true
                    toastMessage=event.message
                }
            }
        }
    }

    //CONTENEDOR FORM
    Box(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        //INICIO DE FORM
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp,30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Formulario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Registro de usuario",
                    style = StyleTextTituloHeader,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign= TextAlign.Start,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Registra un nuevo usuario para administrar.",
                    style = StyleTextSubHeader,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextFieldClasic(
                    value=uiState.fullname,
                    onValueChange = { viewModel.onFullnameChange(it) },
                    onBlur = {viewModel.onFullnameFocusLost()},
                    label = "Nombre completo *",
                    placeholder = "Ej. Juan Perez",
                    isError = uiState.errorFullName != null,
                    errorMessage = uiState.errorFullName,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.userName,
                    onValueChange = {viewModel.onUserNameChange(it)},
                    onBlur = {viewModel.onUserNameFocusLost()},
                    label = "Usuario *",
                    placeholder = "juanperez23",
                    isError = uiState.errorUserName != null,
                    errorMessage = uiState.errorUserName,
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.password,
                    onValueChange = {viewModel.onPasswordChange(it)},
                    onBlur = {viewModel.onPasswordFocusLost()},
                    label = "Contraseña *",
                    placeholder = "********",
                    isError = uiState.errorPassword != null,
                    errorMessage = uiState.errorPassword,
                    isPassword = true,
                    isPasswordVisible = uiState.isPasswordVisible,
                    onPasswordToggleClick = {viewModel.togglePasswordVisibility()},
                    leadingIcon = Icons.Default.Lock,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.confirmPassword,
                    onValueChange = {viewModel.onConfirmPasswordChange(it)},
                    label = "Confirmar contraseña *",
                    placeholder = "********",
                    isError = uiState.errorConfirmPassword != null,
                    errorMessage = uiState.errorConfirmPassword,
                    isPassword = true,
                    isPasswordVisible = uiState.isConfirmPasswordVisible,
                    onPasswordToggleClick = {viewModel.toggleConfirmPasswordVisibility()},
                    leadingIcon = Icons.Default.Password,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    )
                )
                Spacer(modifier = Modifier.height(26.dp))
                CustomButton(
                    text="Agregar usuario",
                    loadingText = "Cargando...",
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.onRegisterClicked() }
                )
            }
        }
        //ANIMACION DE TOAST
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