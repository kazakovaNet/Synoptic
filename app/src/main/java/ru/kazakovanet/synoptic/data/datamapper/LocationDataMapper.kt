package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.LocationEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.LocationDTO

/**
 * Created by NKazakova on 23.07.2020.
 */
object LocationDataMapper {
    fun map(dto: LocationDTO): LocationEntity {
        return LocationEntity(
            city = dto.city,
            country = dto.country,
            lat = dto.lat,
            lon = dto.long,
            region = dto.region,
            timezoneId = dto.timezoneId,
            localtimeEpoch = System.currentTimeMillis() / 1000
        )
    }
}