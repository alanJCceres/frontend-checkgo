package com.example.checkgo.feature_auth.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.ui.components.CustomTextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.checkgo.feature_auth.presentation.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel= viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val postResult by viewModel.postResult.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp,0.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de usuarios", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(20.dp))
        //Formulario
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTextField(
                value=uiState.fullname,
                onValueChange = { viewModel.onFullnameChange(it) },
                onBlur = {viewModel.onFullnameFocusLost()},
                label = "Nombre completo *",
                placeholder = "Jhon doe",
                isError = uiState.errorFullName != null,
                errorMessage = uiState.errorFullName
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=uiState.email,
                onValueChange = {viewModel.onEmailChange(it)},
                onBlur = {viewModel.onEmailFocusLost()},
                label = "Correo eléctronico *",
                placeholder = "jhon@gmail.com",
                isError = uiState.errorEmail != null,
                errorMessage = uiState.errorEmail,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=uiState.userName,
                onValueChange = {viewModel.onUserNameChange(it)},
                onBlur = {viewModel.onUserNameFocusLost()},
                label = "Nombre de usuario *",
                placeholder = "JhonD",
                isError = uiState.errorUserName != null,
                errorMessage = uiState.errorUserName
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
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
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=uiState.confirmPassword,
                onValueChange = {viewModel.onConfirmPasswordChange(it)},
                label = "Repite la contraseña *",
                placeholder = "********",
                isError = uiState.errorConfirmPassword != null,
                errorMessage = uiState.errorConfirmPassword,
                isPassword = true,
                isPasswordVisible = uiState.isConfirmPasswordVisible,
                onPasswordToggleClick = {viewModel.toggleConfirmPasswordVisibility()},
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
                onClick = {viewModel.onRegisterClicked() }
            ) {
                Text("Registrarse")
            }
            postResult?.let {
                Spacer(modifier=Modifier.height(8.dp))
                Text(it,color=if(it.contains("Error")) Color.Red else Color.Green)
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
}