package com.example.checkgo.feature_admin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.checkgo.core.SessionManager.SessionManager
import com.example.checkgo.core.data.localStorage.AppDataStoreManager
import com.example.checkgo.core.data.localStorage.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeAdminViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel(){
    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            sessionManager.clearSession()
            onLogoutComplete()
        }
    }
}