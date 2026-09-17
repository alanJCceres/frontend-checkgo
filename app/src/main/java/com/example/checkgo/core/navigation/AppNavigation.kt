package com.example.checkgo.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.checkgo.feature_admin.presentation.screens.HomeAdminScreen
import com.example.checkgo.feature_admin.presentation.screens.RegisterUserScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterAdminSuccScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination="inicio"){
        composable("inicio"){
            RegisterScreen(navController=navController)
        }
        composable(
            "registerAdminSuccScreen/{userName}/{password}",
            arguments = listOf(
                navArgument("userName") { type = NavType.StringType },
                navArgument("password") { type = NavType.StringType }
            )
        ) {
            RegisterAdminSuccScreen(navController=navController)
        }
        composable("HomeAdmin"){
            HomeAdminScreen(navController=navController)
        }
        composable("registerUser"){
            RegisterUserScreen()
        }
    }
}