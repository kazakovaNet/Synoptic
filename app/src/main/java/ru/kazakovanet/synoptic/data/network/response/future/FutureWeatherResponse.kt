package ru.kazakovanet.synoptic.data.network.response.future

import com.google.gson.annotations.SerializedName
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation

data class FutureWeatherResponse(
    @SerializedName("daily")
    val futureWeatherEntries: List<FutureWeatherEntry>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int
) {
    val futureWeatherLocation: FutureWeatherLocation
        get() = FutureWeatherLocation(lat, lon, timezone, timezoneOffset)
}