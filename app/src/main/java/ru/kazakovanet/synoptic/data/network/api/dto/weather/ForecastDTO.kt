package ru.kazakovanet.synoptic.data.network.api.dto.weather

data class ForecastDTO(
    val code: Int,
    val date: Long,
    val day: String,
    val high: Int,
    val low: Int,
    val text: String
)