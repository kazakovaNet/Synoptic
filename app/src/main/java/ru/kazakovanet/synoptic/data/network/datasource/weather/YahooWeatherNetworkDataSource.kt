package ru.kazakovanet.synoptic.data.network.datasource.weather

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.api.dto.weather.YahooWeatherResponseDTO

/**
 * Created by NKazakova on 22.07.2020.
 */
interface YahooWeatherNetworkDataSource {
    val downloadedWeather: LiveData<YahooWeatherResponseDTO>

    suspend fun fetchWeather(
        location: String,
        units: String
    )
}