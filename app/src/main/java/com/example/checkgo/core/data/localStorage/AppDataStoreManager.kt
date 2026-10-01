package com.example.checkgo.core.data.localStorage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")
class AppDataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val IS_FIRST_TIME_LOGIN = booleanPreferencesKey("is_first_time_login")

    suspend fun saveNeedsRegistration(needs: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_TIME_LOGIN] = needs
        }
    }
    suspend fun getNeedsRegistration(): Boolean {
        return context.dataStore.data.map { preferences ->
            preferences[IS_FIRST_TIME_LOGIN] ?: false
        }.first()
    }
}