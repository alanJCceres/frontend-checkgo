package com.example.checkgo.feature_admin.presentation.screens

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

@Composable
fun HomeAdminScreen(

) {
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
            Text(text = "HOLA MUNDO DESDE HOME ADMIN")
        }
    }

}