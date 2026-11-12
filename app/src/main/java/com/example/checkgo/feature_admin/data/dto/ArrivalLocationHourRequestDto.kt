package com.example.checkgo.feature_admin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArrivalLocationHourRequestDto (
    val startTime: String,
    val endTime: String,
)
