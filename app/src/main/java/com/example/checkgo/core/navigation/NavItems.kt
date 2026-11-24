package com.example.checkgo.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Cases
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItems(
    val title: String,
    val icon: ImageVector,
    val route: String
)
// Opciones del menú para el rol ADMIN
val adminNavItems = listOf(
    NavItems(
        title = "Inicio",
        icon = Icons.Default.Home,
        route = "homeAdmin"
    ),
    NavItems(
        title = "usuarios",
        icon = Icons.Default.AccessTimeFilled,
        route = "listUsers"
    ),
)

// Opciones del menú para el rol USUARIO
val userNavItems = listOf(
    NavItems(
        title = "Inicio",
        icon = Icons.Default.Home,
        route = "homeUser"
    ),
    NavItems(
        title = "Registros",
        icon = Icons.Default.Book,
        route = "reportsUser"
    ),
    NavItems(
        title = "Ajustes",
        icon = Icons.Default.Settings,
        route = "settingsUser"
    )
)