package ru.kazakovanet.synoptic.data.network.datasource.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.dto.weather.YahooWeatherResponseDTO
import ru.kazakovanet.synoptic.data.network.api.yahoo.YahooWeatherApiService
import ru.kazakovanet.synoptic.internal.NoConnectivityException

class YahooWeatherNetworkDataSourceImpl(
    private val apiService: YahooWeatherApiService
) : YahooWeatherNetworkDataSource {

    private val _downloadedWeather = MutableLiveData<YahooWeatherResponseDTO>()
    override val downloadedWeather: LiveData<YahooWeatherResponseDTO>
        get() = _downloadedWeather

    override suspend fun fetchWeather(location: String, units: String) {
        try {
            val fetchedCurrentWeather = apiService.getWeather(location, units).await()
            _downloadedWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}