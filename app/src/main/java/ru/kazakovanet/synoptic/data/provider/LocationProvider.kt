package ru.kazakovanet.synoptic.data.provider

import ru.kazakovanet.synoptic.data.db.entity.LocationEntity

/**
 * Created by NKazakova on 01.07.2020.
 */
interface LocationProvider {
    suspend fun hasLocationChanged(location: LocationEntity): Boolean
    suspend fun getPreferredLocationString(): Map<String, String>
}