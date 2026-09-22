package com.example.checkgo.core.data.localStorage

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveTokens(accessToken: String, refreshToken: String, role: String) {
        sharedPreferences.edit()
            .putString("ACCESS_TOKEN", accessToken)
            .putString("REFRESH_TOKEN", refreshToken)
            .putString("ROLE", role)
            .apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString("ACCESS_TOKEN", null)
    fun getRefreshToken(): String? = sharedPreferences.getString("REFRESH_TOKEN", null)
    fun getRole(): String? = sharedPreferences.getString("ROLE",null)
    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}