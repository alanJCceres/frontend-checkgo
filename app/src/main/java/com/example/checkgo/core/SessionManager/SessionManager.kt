package com.example.checkgo.core.SessionManager

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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

