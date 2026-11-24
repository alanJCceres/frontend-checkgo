package com.example.checkgo.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.checkgo.core.navigation.NavItems
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.ui.theme.LightColorPrimary

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    items: List<NavItems>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 24.dp, end = 24.dp, bottom = 20.dp, top = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .width((items.size * 88).dp)
                .height(55.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    clip = false,
                    ambientColor = Color.Black.copy(alpha = 0.25f),
                    spotColor = Color.Black.copy(alpha = 0.35f)
                )
                .clip(CircleShape)
                .background(Color.White),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            items.forEach { item ->
                val isSelected = currentRoute == item.route
                val lineColor by animateColorAsState(
                    targetValue = if (isSelected) LightColorPrimary else Color.Transparent,
                    label = "selectedLine"
                )
                NavigationBarItem(
                    modifier = Modifier.drawBehind {
                        val lineWidth = 20.dp.toPx()
                        val lineHeight = 3.dp.toPx()
                        drawRoundRect(
                            color = lineColor,
                            topLeft = Offset(
                                x = (size.width - lineWidth) / 2,
                                y = size.height - lineHeight - 4.dp.toPx()
                            ),
                            size = Size(lineWidth, lineHeight),
                            cornerRadius = CornerRadius(lineHeight / 2)
                        )
                    },
                    selected = isSelected,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
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
                            modifier = Modifier.offset(y = 2.dp),
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    label = { Text(
                        modifier = Modifier
                        .offset(y = (-6).dp),
                        text = item.title,
                        fontSize = 10.sp
                    )},
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightColorPrimary,
                        selectedTextColor = LightColorPrimary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}