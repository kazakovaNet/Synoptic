package ru.kazakovanet.synoptic.data.network.datasource.weather

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.api.dto.weather.WeatherResponseDTO

/**
 * Created by NKazakova on 22.07.2020.
 */
interface WeatherNetworkDataSource {
    val downloadedWeather: LiveData<WeatherResponseDTO>

    suspend fun fetchWeather(
        location: Map<String, String>,
        units: String
    )
}