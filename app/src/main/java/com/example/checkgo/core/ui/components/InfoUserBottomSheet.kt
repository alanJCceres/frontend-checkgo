package com.example.checkgo.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.data.dto.UserResponseDto
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextBody
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoUserBottomSheet(
    user: UserResponseDto,
    onDismissRequest: () -> Unit
) {
    val modoOscuro = isSystemInDarkTheme()
    val circleBackground = Color(0xFFEEEEEE)

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val badgeColors = if (user.active) BadgeDefaults.Green else BadgeDefaults.Gray
    val badgeText = if (user.active) "Activo" else "Inactivo"

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        // color fondo semitransparente
        scrimColor = Color.Black.copy(alpha = 0.5f),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de usuario
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(circleBackground),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    text = user.initials)
            }

            Spacer(modifier = Modifier.height(26.dp))

            //BADGE
            CustomBadge(
                text = badgeText,
                colors = badgeColors
            )

            Spacer(modifier = Modifier.height(26.dp))

            //NOMBRE DE USUARIO
            Text(
                text = user.fullname,
                style = StyleTextTituloBody,
                color = MaterialTheme.colorScheme.secondary,
                textAlign= TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(26.dp))

            //DATOS DEL USUARIO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Usuario: ")
                Text(
                    text = user.userName,
                    fontWeight = FontWeight.Medium,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Email: ")
                Text(
                    text = user.email?: "N/A",
                    fontWeight = FontWeight.Medium,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Rol: ")
                Text(
                    text = user.rol,
                    fontWeight = FontWeight.Medium,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Id instalación: ")
                Text(
                    text = user.androidId?:"N/A",
                    fontWeight = FontWeight.Medium,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            //BOTON PARA EDITAR
            CustomButton(
                text="Editar información",
                onClick = { onDismissRequest() }
            )
            Spacer(modifier = Modifier.height(26.dp))
        }
    }
}