package com.example.checkgo.feature_admin.data.dto

import com.example.checkgo.core.data.enum.Day
import kotlinx.serialization.Serializable

@Serializable
data class ArrivalLocationDayRequestDto (
    val day: Day,
    val enabled: Boolean,
    val intervalos: List<ArrivalLocationHourRequestDto>
)