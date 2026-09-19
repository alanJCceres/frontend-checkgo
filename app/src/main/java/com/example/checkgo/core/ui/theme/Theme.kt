package com.example.checkgo.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightColorPrimary,
    secondary = LightColorSecondary,
    tertiary = LightColorWhite,
    background = DarkColorBackground,
    onBackground = DarkTextColorPrincipal,     // Texto principal
    onSurface = DarkTextColorPrincipal,        // Texto principal sobre tarjetas
    onSurfaceVariant = DarkTextColorSecundario  // Texto secundario y Labels
)

private val LightColorScheme = lightColorScheme(
    primary = LightColorPrimary,
    secondary = LightColorSecondary,
    tertiary = LightTextColorSecundario,             //para el texto cuando tiene backgroud debajo
    background = LightColorBackground,
    onBackground = LightTextColorPrincipal,     // Texto principal
    onSurface = LightTextColorPrincipal,        // Texto principal sobre tarjetas
    onSurfaceVariant = LightTextColorSecundario  // Texto secundario y Labels
    /* Other default colors to override
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CheckgoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = LightColorScheme, //cambiar a colorScheme para que detecte si el celular esta en modo oscuro o no
        typography = CheckGoTypography,
        content = content
    )
}