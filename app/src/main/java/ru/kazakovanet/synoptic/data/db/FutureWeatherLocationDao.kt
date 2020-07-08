package ru.kazakovanet.synoptic.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.FUTURE_WEATHER_LOCATION_ID
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation

/**
 * Created by NKazakova on 05.07.2020.
 */
@Dao
interface FutureWeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(futureWeatherLocation: FutureWeatherLocation)

    @Query("select * from future_weather_location where id = $FUTURE_WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<FutureWeatherLocation>

    @Query("select * from future_weather_location where id = $FUTURE_WEATHER_LOCATION_ID")
    fun getLocationNonLive(): FutureWeatherLocation?
}