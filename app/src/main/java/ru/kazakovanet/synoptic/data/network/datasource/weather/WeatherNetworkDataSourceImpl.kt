package ru.kazakovanet.synoptic.data.network.datasource.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.dto.weather.WeatherResponseDTO
import ru.kazakovanet.synoptic.data.network.api.service.weather.WeatherApiService
import ru.kazakovanet.synoptic.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val apiService: WeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedWeather = MutableLiveData<WeatherResponseDTO>()
    override val downloadedWeather: LiveData<WeatherResponseDTO>
        get() = _downloadedWeather

    override suspend fun fetchWeather(location: Map<String, String>, units: String) {
        try {
            val fetchedCurrentWeather: WeatherResponseDTO =
                if (location.containsKey("location")) {
                    apiService.getWeatherByLocation(location.getValue("location"), units).await()
                } else {
                    apiService.getWeatherByLatLon(
                        location.getValue("lat"),
                        location.getValue("lon"),
                        units
                    ).await()
                }
            _downloadedWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}