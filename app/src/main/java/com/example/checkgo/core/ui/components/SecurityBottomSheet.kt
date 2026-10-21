package com.example.checkgo.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.filled.Security
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
import com.example.checkgo.core.ui.theme.DarkTextColorSecundario
import com.example.checkgo.core.ui.theme.LightTextColorSecundario
import com.example.checkgo.core.ui.theme.StyleTextBody
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityBottomSheet(
    onDismissRequest: () -> Unit
) {
    val modoOscuro = isSystemInDarkTheme()
    val circleBackground = Color(0xFFEEEEEE) // Gris muy suave
    val infoBoxBackground = Color(0xFFF9FAFB) // Gris para la caja de info

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

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

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(circleBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Escudo de seguridad",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Seguridad de registro activada",
                style = StyleTextTituloBody,
                color = MaterialTheme.colorScheme.secondary,
                textAlign= TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = buildAnnotatedString {
                    append("Para tu protección y prevención de fraude, hemos vinculado automáticamente tu ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)) {
                        append("ID de instlación único ")
                    }
                    append("a tu cuenta.")
                },
                style = StyleTextBody,
                color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                modifier = Modifier.fillMaxWidth(),
                textAlign= TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(26.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(infoBoxBackground)
                    .padding(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Información",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Esto garantiza que solo tú puedes acceder desde este dispositivo, asegurando la integridad de tus registros de asistencia.",
                    style = StyleTextSubHeader,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = if(modoOscuro) LightTextColorSecundario else DarkTextColorSecundario,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton(
                text="Entendido, gracias",
                onClick = { onDismissRequest() }
            )
            Spacer(modifier = Modifier.height(26.dp))
        }
    }
}