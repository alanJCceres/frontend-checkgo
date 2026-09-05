package com.example.checkgo.feature_admin.domain.validators

object FullNameValidator {
    fun validate(name: String): String? {
        return when {
            name.isBlank() -> "El nombre no puede estar vacío"
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres"
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> "Solo se permiten letras y espacios"
            else -> null
        }
    }
}

object EmailValidator {
    fun validate(email: String): String? {
        if (!email.contains("@")) return "Correo inválido"
        return null
    }
}