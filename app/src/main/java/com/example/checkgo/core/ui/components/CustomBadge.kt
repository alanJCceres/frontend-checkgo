package com.example.checkgo.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BadgeColors(
    val containerColor: Color, // Color de fondo
    val contentColor: Color    // Color del texto
)

// 2. Presets con la paleta de colores para todos los casos
object BadgeDefaults {
    val Green = BadgeColors(
        containerColor = Color(0xFFE8F5E9),
        contentColor = Color(0xFF2E7D32)
    )
    val Red = BadgeColors(
        containerColor = Color(0xFFFFEBEE),
        contentColor = Color(0xFFC62828)
    )
    val Orange = BadgeColors(
        containerColor = Color(0xFFFFF3E0),
        contentColor = Color(0xFFE65100)
    )
    val Yellow = BadgeColors(
        containerColor = Color(0xFFFFFDE7),
        contentColor = Color(0xFFF57F17)
    )
    val Gray = BadgeColors(
        containerColor = Color(0xFFF5F5F5),
        contentColor = Color(0xFF616161)
    )
    val Black = BadgeColors(
        containerColor = Color(0xFF212121),
        contentColor = Color(0xFFFFFFFF)
    )
    val Blue = BadgeColors(
        containerColor = Color(0xFFE3F2FD),
        contentColor = Color(0xFF1565C0)
    )
}
@Composable
fun CustomBadge(
    text: String,
    modifier: Modifier = Modifier,
    colors: BadgeColors = BadgeDefaults.Gray,
    cornerRadius: Dp = 16.dp
) {
    Surface(
        shape = RoundedCornerShape(cornerRadius),
        color = colors.containerColor,
        contentColor = colors.contentColor,
        modifier = modifier
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}