package com.example.checkgo.feature_auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun RegisterAdminSuccScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .systemBarsPadding() // <-- Empuja el contenido hacia adentro respetando el status bar y navigation bar
            .padding(20.dp,50.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = {
                //navController.navigate("registerUser")
            }
        ) {
            Text("Continuar")
        }
    }
}