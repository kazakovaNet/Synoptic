package ru.kazakovanet.synoptic.data.db.entity

import org.threeten.bp.ZonedDateTime

/**
 * Created by NKazakova on 06.07.2020.
 */
interface WeatherLocation {
    val lat: Double
    val lon: Double
    val zonedDateTime: ZonedDateTime
}