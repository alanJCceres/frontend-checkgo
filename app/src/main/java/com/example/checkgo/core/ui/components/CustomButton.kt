package com.example.checkgo.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loadingText: String? = null,
    isLoading: Boolean = false,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    isPrimary: Boolean = true
) {
    // Colores base
    val containerColor = if (isPrimary) MaterialTheme.colorScheme.secondary else Color.Transparent
    val contentColor = if (isPrimary) Color.White else MaterialTheme.colorScheme.secondary

    // Colores cuando está cargando (deshabilitado)
    val disabledContainerColor = containerColor.copy(alpha = 0.6f)
    val disabledContentColor = contentColor.copy(alpha = 0.6f)

    // Borde
    val borderAlpha = if (isLoading) 0.6f else 1f
    val border = if (isPrimary) null else BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = borderAlpha))

    // Elevación (sombras)
    val elevation = if (isPrimary) {
        ButtonDefaults.buttonElevation()
    } else {
        ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp)
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(20.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        border = border,
        elevation = elevation,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (isLoading) {
            // --- ESTADO CARGANDO ---
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = disabledContentColor, // El loader toma el color del texto para que combine
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = loadingText ?: text,
                fontWeight = FontWeight.Bold
            )
        } else {
            // --- ESTADO NORMAL ---
            if (leftIcon != null) {
                Icon(
                    imageVector = leftIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(
                text = text,
                fontWeight = FontWeight.Bold
            )

            if (rightIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = rightIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}