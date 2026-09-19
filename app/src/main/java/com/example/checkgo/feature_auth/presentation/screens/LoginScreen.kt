package com.example.checkgo.feature_auth.presentation.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.checkgo.R
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.components.CustomTextField
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.feature_auth.presentation.viewmodel.LoginUiEvent
import com.example.checkgo.feature_auth.presentation.viewmodel.LoginViewModel
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterSuccUiEvent
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {

    val modoOscuro = isSystemInDarkTheme()
    val uiState by viewModel.uiState.collectAsState()
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var isErrorToast by remember {mutableStateOf(true)}
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Efecto para ocultar y mostrar la barra de estado
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val window = (context as? Activity)?.window
        val insetsController = window?.let { WindowCompat.getInsetsController(it, it.decorView) }
        if (insetsController != null) {
            // Oculta la barra superior de estado
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            //Permite que el usuario deslice desde arriba para ver la hora temporalmente
            insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        onDispose {
            // Restaura la barra de estado cuando el usuario sale del Login (pasa al Home)
            insetsController?.show(WindowInsetsCompat.Type.statusBars())
        }
    }

    LaunchedEffect(toastMessage) {
        if (toastMessage!=null){
            delay(4000)
            toastMessage=null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect{ event ->
            when(event){
                is LoginUiEvent.Navigate -> {
                    navController.navigate(event.route){
                        popUpTo("login"){inclusive=true}
                    }
                }
                is LoginUiEvent.ShowErrorToast ->{
                    isErrorToast=true
                    toastMessage=event.message
                }
            }
        }
    }

    //INTERFAZ
    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.60f)
            ) {
                Image(
                    painter = painterResource(R.drawable.fondo_login),
                    contentDescription = "Fondo de Login",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Bienvenido",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Inicia sesión para continuar",
                        color = Color.White,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium,
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.40f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(
                    value=uiState.userName,
                    onValueChange = {viewModel.onUserNameChange(it)},
                    onBlur = {viewModel.onUserNameFocusLost()},
                    label = "Usuario",
                    placeholder = "user123",
                    isError = uiState.errorUserName != null,
                    errorMessage = uiState.errorUserName,
                    leadingIcon = Icons.Default.Person
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomTextField(
                    value=uiState.password,
                    onValueChange = {viewModel.onPasswordChange(it)},
                    onBlur = {viewModel.onPasswordFocusLost()},
                    label = "Contraseña",
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
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if(modoOscuro) MaterialTheme.colorScheme.primary else DarkTextColorSecundario,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable (enabled = !uiState.isLoading){
                            //navController.navigate("pantallaRecuperar")
                        }
                )
                Spacer(modifier = Modifier.height(10.dp))
                CustomButton(
                    text="Iniciar sesión",
                    loadingText = "Cargando...",
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.onLoginClicked() }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("¿Eres admin y no tienes cuenta?",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Crear cuenta",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable (
                            enabled = !uiState.isLoading,
                        ){
                            navController.navigate("registerAdmin")
                        }
                    )
                }
            }
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