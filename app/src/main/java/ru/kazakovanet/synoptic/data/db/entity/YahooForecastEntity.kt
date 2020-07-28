package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by NKazakova on 22.07.2020.
 */
@Entity(tableName = "yahoo_forecast", indices = [Index(value = ["date"], unique = true)])
data class YahooForecastEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val code: Int,
    val date: Long,
    val day: String,
    val high: Int,
    val low: Int,
    val text: String
)