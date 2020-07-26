package ru.kazakovanet.synoptic.data.network.api.dto.weather


import com.google.gson.annotations.SerializedName

data class LocationDTO(
    val city: String,
    val country: String,
    val lat: Double,
    val long: Double,
    val region: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    val woeid: Int
)