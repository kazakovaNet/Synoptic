package ru.kazakovanet.synoptic.data.network.response

import com.google.gson.annotations.SerializedName
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.WeatherLocation

data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: WeatherLocation
)