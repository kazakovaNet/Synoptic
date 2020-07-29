package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.ForecastDTO

/**
 * Created by NKazakova on 23.07.2020.
 */
object ForecastDataMapper {
    fun map(list: List<ForecastDTO>): List<ForecastWeatherEntity> {
        return list.map { dto ->
            ForecastWeatherEntity(
                id = null,
                date = dto.date,
                day = dto.day,
                low = dto.low,
                high = dto.high,
                conditionCode = dto.code,
                conditionText = dto.text
            )
        }
    }
}