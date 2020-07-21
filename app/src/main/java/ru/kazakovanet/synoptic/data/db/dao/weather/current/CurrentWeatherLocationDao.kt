package ru.kazakovanet.synoptic.data.db.dao.weather.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherLocation
import ru.kazakovanet.synoptic.data.db.entity.WEATHER_LOCATION_ID

/**
 * Created by NKazakova on 01.07.2020.
 */
@Dao
interface CurrentWeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(currentWeatherLocation: CurrentWeatherLocation)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<CurrentWeatherLocation>

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocationNonLive(): CurrentWeatherLocation?
}