package com.example.checkgo.feature_auth.domain.validators

object FullNameValidator {
    fun validate(name: String): String? {
        return when {
            name.isBlank() -> "El nombre no puede estar vacío."
            name.length < 3 -> "El nombre debe tener al menos 3 caracteres."
            name.length > 100 -> "El nombre no debe tener más de 100 carácteres."
            !name.matches(Regex("^[\\p{L}\\s]+$")) -> "Solo se permiten letras y espacios."
            else -> null
        }
    }
}
object EmailValidator {
    fun validate(email: String): String? {
        return when {
            email.isBlank() -> "El email no puede estar vacío."
            !email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) -> "Correo inválido."
            else -> null
        }
    }
}
object UserNameValidator {
    fun validate(userName: String): String? {
        return when {
            userName.isBlank() -> "El nombre de usuario no puede estar vacío."
            userName.length < 3 -> "El nombre de usuario debe tener al menos 3 caracteres."
            userName.length > 20 -> "El nombre de usuario no debe tener más de 20 carácteres."
            else -> null
        }
    }
}
object PasswordValidator {
    fun validate(password: String): String? {
        return when {
            password.isBlank() -> "La contraseña no puede estar vacío."
            password.length < 8 -> "La contraseña debe tener al menos 8 caracteres."
            password.length > 20 -> "La contraseña no debe tener más de 20 carácteres."
            !password.matches(Regex("^\\S+$")) -> "contraseña inválida."
            else -> null
        }
    }
}