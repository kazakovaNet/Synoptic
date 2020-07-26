package ru.kazakovanet.synoptic.data.network.api.dto.weather


import com.google.gson.annotations.SerializedName

data class WindDTO(
    val chill: Int,
    val direction: Int,
    val speed: Double
)