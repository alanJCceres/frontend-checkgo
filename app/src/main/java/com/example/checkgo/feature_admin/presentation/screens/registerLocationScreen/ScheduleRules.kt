package com.example.checkgo.feature_admin.presentation.screens.registerLocationScreen

import com.example.checkgo.core.data.enum.Day
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationDayRequestDto
import com.example.checkgo.feature_admin.data.dto.ArrivalLocationHourRequestDto
import com.example.checkgo.feature_admin.presentation.viewmodel.Step2State

enum class ScheduleMode { BOTH, START_ONLY, END_ONLY }

fun Step2State.toScheduleMode(): ScheduleMode = when {
    arrivalOnly -> ScheduleMode.START_ONLY
    exitOnly -> ScheduleMode.END_ONLY
    else -> ScheduleMode.BOTH
}

const val MAX_INTERVALS_PER_DAY = 4

/** Siempre los 7 días, todos apagados. */
fun defaultWeek(): List<ArrivalLocationDayRequestDto> =
    Day.entries.map { ArrivalLocationDayRequestDto(day = it, enabled = false, intervalos = emptyList()) }

/** Una fila está completa según los campos que el modo exige. */
fun ArrivalLocationHourRequestDto.isComplete(mode: ScheduleMode): Boolean = when (mode) {
    ScheduleMode.BOTH -> startTime.isNotBlank() && endTime.isNotBlank()
    ScheduleMode.START_ONLY -> startTime.isNotBlank()
    ScheduleMode.END_ONLY -> endTime.isNotBlank()
}

/** Limpia el campo que ya no aplica y elimina repetidos (si el usuario cambió de modo en el step 2). */
fun List<ArrivalLocationDayRequestDto>.normalizedFor(mode: ScheduleMode) = map { day ->
    day.copy(
        intervalos = day.intervalos.map {
            when (mode) {
                ScheduleMode.BOTH -> it
                ScheduleMode.START_ONLY -> it.copy(endTime = "")
                ScheduleMode.END_ONLY -> it.copy(startTime = "")
            }
        }.distinct()
    )
}

private fun Day.label(): String = when (this) {
    Day.Lunes -> "Lunes"
    Day.Martes -> "Martes"
    Day.Miércoles -> "Miércoles"
    Day.Jueves -> "Jueves"
    Day.Viernes -> "Viernes"
    Day.Sábado -> "Sábado"
    Day.Domingo -> "Domingo"
}