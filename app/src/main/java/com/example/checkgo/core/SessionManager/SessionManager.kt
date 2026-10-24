package com.example.checkgo.core.SessionManager

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

//clase que reune todas las implementaciones de SessionClearable en un solo lugar y limpia todo lo que fue implementado
@Singleton
class SessionManager @Inject constructor(
    private val clearables: Set<@JvmSuppressWildcards SessionClearable>
) {
    suspend fun clearSession() = withContext(NonCancellable) {
        clearables.forEach { clearable ->
            try {
                clearable.clear()
            } catch (e: Exception) {

            }
        }
    }
}

