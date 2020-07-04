package ru.kazakovanet.synoptic.data.network

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.response.CurrentWeatherResponse
import ru.kazakovanet.synoptic.data.network.response.FutureWeatherResponse

/**
 * Created by NKazakova on 27.06.2020.
 */
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units: String
    )

    suspend fun fetchFutureWeather(
        lat: Double,
        lon: Double
    )
}