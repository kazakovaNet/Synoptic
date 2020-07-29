package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by NKazakova on 22.07.2020.
 */
@Entity(tableName = "forecast_weather", indices = [Index(value = ["date"], unique = true)])
data class ForecastWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val date: Long,
    val day: String,
    val low: Int,
    val high: Int,
    val conditionCode: Int,
    val conditionText: String
)