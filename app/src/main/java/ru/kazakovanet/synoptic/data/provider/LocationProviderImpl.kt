package ru.kazakovanet.synoptic.data.provider

import ru.kazakovanet.synoptic.data.db.entity.WeatherLocation

class LocationProviderImpl : LocationProvider {
    override suspend fun hasLocationChanged(lastWeatherLocation: WeatherLocation): Boolean {
        return true
    }

    override suspend fun getPreferredLocationString(): String {
        return "Moscow"
    }
}