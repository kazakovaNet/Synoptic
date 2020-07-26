package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

/**
 * Created by NKazakova on 22.07.2020.
 */
const val LOCATION_ID = 0

@Entity(tableName = "yahoo_location")
data class YahooLocationEntity(
    val city: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val region: String,
    @ColumnInfo(name = "timezone_id")
    val timezoneId: String,
    @ColumnInfo(name = "local_time_epoch")
    val localtimeEpoch: Long
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = LOCATION_ID
    val zonedDateTime: ZonedDateTime
        get() {
            val instant = Instant.ofEpochSecond(localtimeEpoch)
            val zoneId = ZoneId.of(timezoneId)
            return ZonedDateTime.ofInstant(instant, zoneId)
        }
}