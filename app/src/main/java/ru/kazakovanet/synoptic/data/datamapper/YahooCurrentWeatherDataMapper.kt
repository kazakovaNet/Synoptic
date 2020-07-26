package ru.kazakovanet.synoptic.data.datamapper

import ru.kazakovanet.synoptic.data.db.entity.YahooWeatherEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.CurrentObservationDTO

/**
 * Created by NKazakova on 23.07.2020.
 */
object YahooCurrentWeatherDataMapper {
    fun map(dto: CurrentObservationDTO): YahooWeatherEntity {
        return YahooWeatherEntity(
            sunrise = dto.astronomy.sunrise,
            sunset = dto.astronomy.sunset,
            humidity = dto.atmosphere.humidity,
            visibility = dto.atmosphere.visibility,
            pressure = dto.atmosphere.pressure,
            rising = dto.atmosphere.rising,
            temperature = dto.condition.temperature,
            conditionCode = dto.condition.code,
            conditionText = dto.condition.text,
            date = dto.pubDate,
            windSpeed = dto.wind.speed
        )
    }
}