package ru.kazakovanet.synoptic.data.network.api.dto.weather


import com.google.gson.annotations.SerializedName

data class AtmosphereDTO(
    val humidity: Int,
    val pressure: Double,
    val rising: Int,
    val visibility: Double
)