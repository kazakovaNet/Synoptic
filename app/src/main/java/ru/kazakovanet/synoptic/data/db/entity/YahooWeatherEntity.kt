package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by NKazakova on 22.07.2020.
 */
const val WEATHER_ID = 0

@Entity(tableName = "yahoo_current_weather")
data class YahooWeatherEntity(
    val sunrise: String,
    val sunset: String,
    val humidity: Int, // влажность
    val visibility: Double, // видимость
    val pressure: Double, // барометрическое давление
    val rising: Int, // состояние барометрического давления: устойчивое (0), повышающееся (1) или понижающееся (2)
    val temperature: Int,
    @ColumnInfo(name = "condition_code")
    val conditionCode: Int,
    @ColumnInfo(name = "condition_text")
    val conditionText: String,
    val date: Long,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_ID
}