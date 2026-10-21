package com.example.checkgo.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkgo.core.data.dto.UserResponseDto

@Composable
fun CardUser(
    user: UserResponseDto,
    onOpenInfo: () -> Unit
) {
    val badgeColors = if (user.active) BadgeDefaults.Green else BadgeDefaults.Gray
    val badgeText = if (user.active) "Activo" else "Inactivo"
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFFD5D5D5),
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(15.dp)
            .clickable { onOpenInfo() }
            ,
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color(0xFFecf2f7), shape = RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            Text(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                text = user.initials)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                text = user.fullname)
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontSize = 14.sp,
                text = "@"+user.userName)

            if(user.email!=null){
                Text(
                    fontSize = 14.sp,
                    color= Color.Gray,
                    text = user.email)
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        CustomBadge(
            text = badgeText,
            colors = badgeColors
        )
    }
}