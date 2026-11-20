package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkgo.R
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.core.ui.components.CustomTopToast
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloHeader
import kotlinx.serialization.Serializable

@Serializable object RegisterLocationSuccessRoute

@Composable
fun RegisterLocationSuccessScreen(onFinish: () -> Unit) {
    val modoOscuro = isSystemInDarkTheme()
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
                        text="¡Ubicación creada!",
                        style = StyleTextTituloHeader,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text="Tu punto de llegada ha sido creado exitosamente.",
                        style = StyleTextSubHeader,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text="Ya puedes empezar a tracear a tus usuarios asignados a esta ubicación",
                        style = StyleTextSubHeader,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario
                    )
                }
            }
            CustomButton(
                text="Continuar",
                onClick = onFinish
            )
        }
    }
}