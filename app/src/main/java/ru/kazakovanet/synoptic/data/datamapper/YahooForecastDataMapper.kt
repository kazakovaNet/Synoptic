package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.YahooForecastEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.ForecastDTO

/**
 * Created by NKazakova on 23.07.2020.
 */
object YahooForecastDataMapper {
    fun map(list: List<ForecastDTO>): List<YahooForecastEntity> {
        return list.map { dto ->
            YahooForecastEntity(
                id = null,
                code = dto.code,
                date = dto.date,
                day = dto.day,
                high = dto.high,
                low = dto.low,
                text = dto.text
            )
        }
    }
}