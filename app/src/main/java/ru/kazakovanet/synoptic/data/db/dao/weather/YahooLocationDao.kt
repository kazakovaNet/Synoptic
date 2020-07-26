package ru.kazakovanet.synoptic.data.db.dao.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.LOCATION_ID
import ru.kazakovanet.synoptic.data.db.entity.YahooLocationEntity

/**
 * Created by NKazakova on 01.07.2020.
 */
@Dao
interface YahooLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(location: YahooLocationEntity)

    @Query("select * from yahoo_location where id = $LOCATION_ID")
    fun getLocation(): LiveData<YahooLocationEntity>

    @Query("select * from yahoo_location where id = $LOCATION_ID")
    fun getLocationNonLive(): YahooLocationEntity?
}