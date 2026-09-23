package com.example.checkgo.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.checkgo.core.navigation.NavItems

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    items: List<NavItems>
) {
    NavigationBar(
        containerColor = Color(0xFF121212)
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Volver al destino inicial del grafo para no acumular pantallas
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Evita múltiples copias de la misma pantalla si se presiona varias veces
                            launchSingleTop = true
                            // Restaura el estado cuando se vuelve a seleccionar un ítem
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color(0xFF0B766B),
                    indicatorColor = Color(0xFF0B766B),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}