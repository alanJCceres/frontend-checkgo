package com.example.checkgo.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.checkgo.feature_admin.presentation.screens.HomeAdminScreen
import com.example.checkgo.feature_admin.presentation.screens.RegisterUserScreen
import com.example.checkgo.feature_auth.presentation.screens.LoginScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterAdminSuccScreen
import com.example.checkgo.feature_auth.presentation.screens.RegisterScreen
import com.example.checkgo.feature_user.presentation.screens.HomeUserScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.checkgo.core.ui.components.AppBottomBar
import com.example.checkgo.feature_admin.presentation.screens.UsersScreen
import com.example.checkgo.feature_user.presentation.screens.ReportsUserScreen

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel= hiltViewModel()
) {
    val isLoading by mainViewModel.isLoading.collectAsState()
    val startDestination by mainViewModel.startDestination.collectAsState()
    val navController = rememberNavController()

    // Obtenemos la ruta actual para saber dónde está el usuario
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isImmersiveScreen = currentRoute == "login" //verificamos si la ruta es login

    // Escuchamos el evento de refresh token expirado, si expiro redirigimos al login automaticamente.
    LaunchedEffect(Unit) {
        mainViewModel.sessionExpiredEvent.collect {
            navController.navigate("login") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF0B766B))
        }
    } else {
        Scaffold(
            //configuracion para hacer completa la pantalla login
            contentWindowInsets = if (isImmersiveScreen) {
                WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
            } else {
                ScaffoldDefaults.contentWindowInsets
            },
            //color del container para hacer el login pantalla completa
            containerColor = if (isImmersiveScreen) Color.Transparent else MaterialTheme.colorScheme.background,
            bottomBar = {
                when {
                    // Si la ruta actual pertenece al flujo Admin
                    currentRoute in adminNavItems.map { it.route } -> {
                        AppBottomBar(
                            navController = navController,
                            currentRoute = currentRoute,
                            items = adminNavItems
                        )
                    }
                    // Si la ruta actual pertenece al flujo Usuario
                    currentRoute in userNavItems.map { it.route } -> {
                        AppBottomBar(
                            navController = navController,
                            currentRoute = currentRoute,
                            items = userNavItems
                        )
                    }
                    // En Login/Registro no mostramos menú
                    else -> {}
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                // --- Pantallas Auth ---
                composable("login"){ LoginScreen(navController=navController) }
                composable("registerAdmin"){ RegisterScreen(navController=navController) }
                composable(
                    "registerAdminSuccScreen/{userName}/{password}",
                    arguments = listOf(
                        navArgument("userName") { type = NavType.StringType },
                        navArgument("password") { type = NavType.StringType }
                    )
                ) { RegisterAdminSuccScreen(navController=navController) }

                // --- Pantallas SUPER ADMIN ---
                composable("homeAdmin"){ HomeAdminScreen(navController=navController) }
                composable("registerUser"){ RegisterUserScreen() }
                composable("listUsers"){ UsersScreen(navController=navController) }

                // --- Pantallas USER ---
                composable("homeUser"){ HomeUserScreen(navController=navController) }
                composable("reportsUser"){ ReportsUserScreen() }

            }
        }
    }
}