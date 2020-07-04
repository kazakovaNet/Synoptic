package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import ru.kazakovanet.synoptic.data.db.LocalDateToLongConverter
import ru.kazakovanet.synoptic.data.db.WeatherListToSingleItemConverter

@Entity(tableName = "future_weather", indices = [Index(value = ["date"], unique = true)])
@TypeConverters(WeatherListToSingleItemConverter::class)
data class FutureWeatherEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    val clouds: Int,

    @SerializedName("dew_point")
    val dewPoint: Double,

    @SerializedName("dt")
    val date: Long,

    val humidity: Int,

    val pressure: Int,

    val rain: Double,

    val sunrise: Int,

    val sunset: Int,

    @Embedded(prefix = "temp_")
    val temp: Temp,

    val uvi: Double,

//    @Embedded(prefix = "weather_description_")
//    val weather: List<Weather>,

    @SerializedName("wind_deg")
    val windDeg: Int,

    @SerializedName("wind_speed")
    val windSpeed: Double
)