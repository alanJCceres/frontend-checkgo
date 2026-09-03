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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.ui.components.CustomTextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    var input1 by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
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
                value=input1,
                onValueChange = {
                    input1=it
                },
                label = "Nombre completo",
                placeholder = "Jhon doe"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=input1,
                onValueChange = {
                    input1=it
                },
                label = "Correo eléctronico",
                placeholder = "jhon@gmail.com"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=input1,
                onValueChange = {
                    input1=it
                },
                label = "Nombre de usuario",
                placeholder = "JhonD"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=input1,
                onValueChange = {
                    input1=it
                },
                label = "Contraseña",
                placeholder = "******"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value=input1,
                onValueChange = {
                    input1=it
                },
                label = "Repite la contraseña",
                placeholder = "******"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                onClick = { }
            ) {
                Text("Registrarse")
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