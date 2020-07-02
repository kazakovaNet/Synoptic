package ru.kazakovanet.synoptic.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.WEATHER_LOCATION_ID
import ru.kazakovanet.synoptic.data.db.entity.WeatherLocation

/**
 * Created by NKazakova on 01.07.2020.
 */
@Dao
interface WeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: WeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<WeatherLocation>
}