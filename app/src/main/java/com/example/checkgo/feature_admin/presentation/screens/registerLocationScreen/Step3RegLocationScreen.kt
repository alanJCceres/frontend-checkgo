package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.checkgo.core.ui.components.CustomButton
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationDayRequestDto
import com.example.checkgo.feature_admin.presentation.components.DayScheduleCard
import com.example.checkgo.feature_admin.presentation.viewmodel.Step3State
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import com.example.checkgo.core.ui.theme.StyleTextBodySmall
import com.example.checkgo.core.ui.theme.StyleTextSubHeader
import com.example.checkgo.core.ui.theme.StyleTextTituloBody

@Composable
fun Step3RegLocationScreen(
    state: Step3State,
    mode: ScheduleMode,
    onDayChange: (ArrivalLocationDayRequestDto) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val days = state.locationSchedules ?: defaultWeek()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Horarios del punto de llegada",
            style = StyleTextTituloBody
        )
        Text(
            text = "Defina los horarios operativos para cada día de la semana.",
            style = StyleTextBodySmall,
            color = Color.Gray
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(days, key = { it.day }) { day ->
                DayScheduleCard(
                    day = day,
                    mode = mode,
                    onDayChange = onDayChange
                )
            }
        }

        state.scheduleError?.let {
            Spacer(Modifier.height(14.dp))
            Text(
                text = it,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomButton(
                text = "Atrás",
                onClick = onBack,
                modifier = Modifier.weight(1f),
                isPrimary = false
            )
            CustomButton(
                text = "Siguiente",
                onClick = onNext,
                modifier = Modifier.weight(1f)
            )
        }
    }
}