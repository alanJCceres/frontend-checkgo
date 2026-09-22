package com.example.checkgo.feature_user.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.checkgo.core.data.localStorage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeUserViewModel @Inject constructor(
    private val tokenManager: TokenManager
): ViewModel(){
    fun logout(onLogoutComplete: () -> Unit) {
        tokenManager.clearAll()
        onLogoutComplete()
    }
}