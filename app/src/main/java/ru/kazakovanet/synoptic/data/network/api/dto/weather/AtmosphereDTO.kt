package ru.kazakovanet.synoptic.data.network.api.dto.weather

data class AtmosphereDTO(
    val humidity: Int,
    val pressure: Double,
    val rising: Int,
    val visibility: Double
)