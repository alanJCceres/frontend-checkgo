package com.example.checkgo.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.checkgo.feature_admin.presentation.screens.HomeAdminScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterAdminSuccScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination="registerAdminSuccScreen"){
        composable("inicio"){
            RegisterScreen(navController=navController)
        }
        composable("registerAdminSuccScreen") {
            RegisterAdminSuccScreen(navController=navController)
        }
        composable("HomeAdmin"){
            HomeAdminScreen()
        }
    }
}