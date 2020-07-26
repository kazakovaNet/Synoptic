package ru.kazakovanet.synoptic.data.network.api.dto.weather


import com.google.gson.annotations.SerializedName

data class AstronomyDTO(
    val sunrise: String,
    val sunset: String
)