package ru.kazakovanet.synoptic.data.network

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.response.CurrentWeatherResponse

/**
 * Created by NKazakova on 27.06.2020.
 */
interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String
    )
}