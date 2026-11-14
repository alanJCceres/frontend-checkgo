package com.example.checkgo.feature_admin.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateLocationRequestDto (
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Int,
    val tolerance: Int,
    val arrivalOnly: Boolean,
    val arrivalAndDeparture: Boolean,
    val exitOnly: Boolean,
    val schedules: List<ArrivalLocationDayRequestDto>,
    val userIds: List<String>
)