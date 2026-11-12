package com.example.checkgo.feature_admin.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationDayRequestDto
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.ScheduleMode
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationHourRequestDto
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.MAX_INTERVALS_PER_DAY
import com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen.isComplete

private data class PickTarget(val index: Int, val isStart: Boolean)

@Composable
fun DayScheduleCard(
    day: ArrivalLocationDayRequestDto,
    mode: ScheduleMode,
    onDayChange: (ArrivalLocationDayRequestDto) -> Unit
) {
    var error by remember { mutableStateOf<String?>(null) }
    var picking by remember { mutableStateOf<PickTarget?>(null) }

    val startLabel = if (mode == ScheduleMode.BOTH) "Inicio" else "Hora de entrada"
    val endLabel = if (mode == ScheduleMode.BOTH) "Fin" else "Hora de salida"

    fun applyTime(target: PickTarget, time: String) {
        val updated = day.intervalos.mapIndexed { i, interval ->
            if (i != target.index) interval
            else if (target.isStart) interval.copy(startTime = time)
            else interval.copy(endTime = time)
        }
        val edited = updated[target.index]
        val isDuplicate = edited.isComplete(mode) &&
                updated.filterIndexed { i, _ -> i != target.index }.any { it == edited }

        if (isDuplicate) {
            error = "Ya existe un horario igual en este día"
        } else {
            error = null
            onDayChange(day.copy(intervalos = updated))
        }
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = day.day.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Switch(
                    checked = day.enabled,
                    onCheckedChange = { enabled ->
                        error = null
                        onDayChange(
                            day.copy(
                                enabled = enabled,
                                // al activar un día sin horarios, aparece una fila vacía lista para llenar
                                intervalos = if (enabled && day.intervalos.isEmpty())
                                    listOf(ArrivalLocationHourRequestDto("", ""))
                                else day.intervalos
                            )
                        )
                    }
                )
            }

            if (day.enabled) {
                day.intervalos.forEachIndexed { index, interval ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (mode != ScheduleMode.END_ONLY) {
                            TimeButton(
                                label = startLabel,
                                value = interval.startTime,
                                onClick = { picking = PickTarget(index, true) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (mode == ScheduleMode.BOTH) Text("–")
                        if (mode != ScheduleMode.START_ONLY) {
                            TimeButton(
                                label = endLabel,
                                value = interval.endTime,
                                onClick = { picking = PickTarget(index, false) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        IconButton(
                            onClick = {
                                error = null
                                onDayChange(
                                    day.copy(intervalos = day.intervalos.filterIndexed { i, _ -> i != index })
                                )
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar horario")
                        }
                    }
                }

                error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                TextButton(
                    onClick = {
                        error = null
                        onDayChange(
                            day.copy(intervalos = day.intervalos + ArrivalLocationHourRequestDto("", ""))
                        )
                    },
                    enabled = day.intervalos.size < MAX_INTERVALS_PER_DAY &&
                            day.intervalos.all { it.isComplete(mode) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Añadir horario (${day.intervalos.size}/$MAX_INTERVALS_PER_DAY)")
                }
            } else {
                Text(
                    text = "No se tomará en cuenta",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    picking?.let { target ->
        val current = day.intervalos.getOrNull(target.index)
        HourPickerDialog(
            initial = (if (target.isStart) current?.startTime else current?.endTime).orEmpty(),
            onConfirm = { time ->
                applyTime(target, time)
                picking = null
            },
            onDismiss = { picking = null }
        )
    }
}