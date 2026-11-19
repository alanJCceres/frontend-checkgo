package com.example.checkgo.core.data.enum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Day {
    @SerialName("Lunes")
    Lunes,
    @SerialName("Martes")
    Martes,
    @SerialName("Miércoles")
    Miércoles,
    @SerialName("Jueves")
    Jueves,
    @SerialName("Viernes")
    Viernes,
    @SerialName("Sábado")
    Sábado,
    @SerialName("Domingo")
    Domingo

}