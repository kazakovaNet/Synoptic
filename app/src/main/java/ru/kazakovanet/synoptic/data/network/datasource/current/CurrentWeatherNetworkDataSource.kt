package ru.kazakovanet.synoptic.data.network.datasource.current

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.response.current.CurrentWeatherResponse

/**
 * Created by NKazakova on 27.06.2020.
 */
interface CurrentWeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
        location: String,
        units: String
    )
}