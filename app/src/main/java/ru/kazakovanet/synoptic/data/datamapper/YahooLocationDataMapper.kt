package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.YahooLocationEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.LocationDTO

/**
 * Created by NKazakova on 23.07.2020.
 */
object YahooLocationDataMapper {
    fun map(dto: LocationDTO): YahooLocationEntity {
        return YahooLocationEntity(
            city = dto.city,
            country = dto.country,
            lat = dto.lat,
            long = dto.long,
            region = dto.region,
            timezoneId = dto.timezoneId,
            localtimeEpoch = System.currentTimeMillis()
        )
    }
}