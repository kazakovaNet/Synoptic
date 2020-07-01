package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val WEATHER_LOCATION_ID = 0

@Entity(tableName = "weather_location")
data class WeatherLocation(
    val name: String,
    val region: String,
    val country: String,
    val lat: String,
    val lon: String,
    @SerializedName("timezone_id")
    val timezoneId: String,
    @SerializedName("localtime_epoch")
    val localtimeEpoch: Long,
    @SerializedName("utc_offset")
    val utcOffset: String,
    val localtime: String
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = WEATHER_LOCATION_ID

    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(localtimeEpoch)
            val zoneId = ZoneId.of(timezoneId)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}