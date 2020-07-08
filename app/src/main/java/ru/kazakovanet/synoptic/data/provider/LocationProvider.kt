package ru.kazakovanet.synoptic.data.provider

import ru.kazakovanet.synoptic.data.db.entity.WeatherLocation

/**
 * Created by NKazakova on 01.07.2020.
 */
interface LocationProvider {
    suspend fun hasLocationChanged(location: WeatherLocation): Boolean
    suspend fun getPreferredLocationString(): String
}