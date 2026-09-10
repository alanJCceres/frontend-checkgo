package com.example.checkgo.core.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun CustomTextFieldClasic(
    value: String,
    onValueChange: (String) -> Unit,
    onBlur: () -> Unit = {},
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordToggleClick: () -> Unit = {}
) {
    var hasHadFocus by remember { mutableStateOf(false) }
    val isDark = isSystemInDarkTheme()
    val leadingIconColor = if (isDark) Color(0xFF6B7280) else Color(0xFF9CA3AF)
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isError) Color.Red else Color.Gray,
            modifier = Modifier.padding(bottom = 6.dp),
            style = TextStyle(
            fontFeatureSettings = "smcp" // "smcp" es el código OpenType para Small Caps
            )
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) hasHadFocus = true
                    else if (!focusState.isFocused && hasHadFocus) onBlur()
                },
            visualTransformation = if(isPassword && !isPasswordVisible){
                PasswordVisualTransformation()
            }else{
                VisualTransformation.None
            },
            trailingIcon = {
                when {
                    isPassword -> {
                        IconButton(onClick = onPasswordToggleClick) {
                            val icon = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            val description = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                            Icon(imageVector = icon, contentDescription = description)
                        }
                    }
                    trailingIcon != null -> {
                        trailingIcon()
                    }
                    else -> null
                }
            },
            leadingIcon = leadingIcon?.let { {
                Icon(imageVector = it,
                    tint = leadingIconColor,
                    contentDescription = null) } },
            placeholder = { Text(text = placeholder) },
            isError = isError,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                // Colores en estado normal
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.0f),
                focusedLabelColor = Color(0xFF0B766B),
                unfocusedLabelColor = Color.Gray,
                unfocusedContainerColor = Color(0xFFecf2f7), //Fondo blanco sin foco
                focusedContainerColor = Color.White, //fondo blanco cuando se haga foco
                errorContainerColor = Color.White, //fondo blanco cuando ocurra un error

                // Colores en estado de error
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red,
                //errorContainerColor = Color(0xFFFFF5F5)
            )
        )
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}