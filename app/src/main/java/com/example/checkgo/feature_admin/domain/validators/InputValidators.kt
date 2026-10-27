package com.example.checkgo.feature_admin.domain.validators


object NewPasswordValidator {
    fun validate(password: String): String? {
        return when {
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
            password.length > 20 -> "La contraseña no debe tener más de 20 carácteres."
            !password.matches(Regex("^\\S+$")) -> "contraseña inválida."
            else -> null
        }
    }
}