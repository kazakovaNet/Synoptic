package ru.kazakovanet.synoptic.data.db.dao.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.LOCATION_ID
import ru.kazakovanet.synoptic.data.db.entity.LocationEntity

/**
 * Created by NKazakova on 01.07.2020.
 */
@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(location: LocationEntity)

    @Query("select * from location where id = $LOCATION_ID")
    fun getLocation(): LiveData<LocationEntity>

    @Query("select * from location where id = $LOCATION_ID")
    fun getLocationNonLive(): LocationEntity?
}