package ru.kazakovanet.synoptic.data.network.datasource.future

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.response.future.FutureWeatherResponse

/**
 * Created by NKazakova on 06.07.2020.
 */
interface FutureWeatherNetworkDataSource {
    val downloadedFutureWeather: LiveData<FutureWeatherResponse>

    suspend fun fetchFutureWeather(
        lat: Double,
        lon: Double
    )
}