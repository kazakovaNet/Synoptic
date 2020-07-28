package ru.kazakovanet.synoptic.data.network.api.dto.weather

data class ConditionDTO(
    val code: Int,
    val temperature: Int,
    val text: String
)