package com.example.checkgo.feature_auth.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomTextFieldClasic
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterUiEvent
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel= viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var isErrorToast by remember {mutableStateOf(true)}
    val context = LocalContext.current

    //Efecto para ocultar el toast automaticamente
    LaunchedEffect(toastMessage) {
        if (toastMessage!=null){
            delay(4000)
            toastMessage=null
        }
    }

    //Efecto para mostrar toast error y/o navegar a sig pantalla
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect{ event ->
            when(event){
                is RegisterUiEvent.Navigate -> {
                    navController.navigate(event.route){
                        popUpTo("inicio"){inclusive=true}
                    }
                }
                is RegisterUiEvent.ShowToast ->{
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
                    text = "Crear cuenta",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign= TextAlign.Start,
                    fontSize = 32.sp)
                Text(
                    text = "Únete a CheckGO hoy mismo y descubre su utilidad.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextFieldClasic(
                    value=uiState.fullname,
                    onValueChange = { viewModel.onFullnameChange(it) },
                    onBlur = {viewModel.onFullnameFocusLost()},
                    label = "nombre completo *",
                    placeholder = "Ej. Juan Perez",
                    isError = uiState.errorFullName != null,
                    errorMessage = uiState.errorFullName,
                    leadingIcon = Icons.Default.Person
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.email,
                    onValueChange = {viewModel.onEmailChange(it)},
                    onBlur = {viewModel.onEmailFocusLost()},
                    label = "correo eléctronico *",
                    placeholder = "correo@ejemplo.com",
                    isError = uiState.errorEmail != null,
                    errorMessage = uiState.errorEmail,
                    leadingIcon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.userName,
                    onValueChange = {viewModel.onUserNameChange(it)},
                    onBlur = {viewModel.onUserNameFocusLost()},
                    label = "usuario *",
                    placeholder = "juanperez23",
                    isError = uiState.errorUserName != null,
                    errorMessage = uiState.errorUserName,
                    leadingIcon = Icons.Default.Person3
                )
                Spacer(modifier = Modifier.height(16.dp))
                CustomTextFieldClasic(
                    value=uiState.password,
                    onValueChange = {viewModel.onPasswordChange(it)},
                    onBlur = {viewModel.onPasswordFocusLost()},
                    label = "contraseña *",
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
                    label = "confirmar contraseña *",
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
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    enabled = !uiState.isLoading,
                    onClick = {viewModel.onRegisterClicked() }
                ) {
                    if(uiState.isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cargando...")
                    }else{
                        Text("Registrarse")
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                //PIE DE PAGINA Sing in
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Ya tienes una cuenta?",
                        color = Color.Gray,
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Iniciar sesión",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            //aqui funcion para llevar a pantalla
                        })
                }
            }
        }
        //ANIMACION DE TOAST
        AnimatedVisibility(
            visible = toastMessage != null,
            // Animación: cae desde arriba y aparece (fade in)
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            // Animación: se desliza hacia arriba y desaparece (fade out)
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            // Lo alineamos en la parte superior del Box (pantalla)
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            // Pasamos el mensaje (si es null no se renderiza por el visible)
            toastMessage?.let { message ->
                CustomTopToast(message = message, isError = isErrorToast)
            }
        }
    }

}