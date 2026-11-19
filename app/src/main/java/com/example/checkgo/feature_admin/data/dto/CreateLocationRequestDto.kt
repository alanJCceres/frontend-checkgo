package com.example.checkgo.feature_admin.data.dto

import kotlinx.serialization.Serializable
@Serializable
data class CreateLocationRequestDto (
    val locationName: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val radio: Int,
    val tolerance: Int,
    val arrivalOnly: Boolean,
    val arrivalAndDeparture: Boolean,
    val exitOnly: Boolean,
    val locationSchedules: List<ArrivalLocationDayRequestDto>,
    val assignedUsers: List<String>
)