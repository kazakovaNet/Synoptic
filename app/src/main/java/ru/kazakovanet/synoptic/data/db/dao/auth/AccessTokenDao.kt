package ru.kazakovanet.synoptic.data.db.dao.auth

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.ACCESS_TOKEN_ID
import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity

/**
 * Created by NKazakova on 14.07.2020.
 */
@Dao
interface AccessTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(accessToken: AccessTokenEntity)

    @Query("select * from access_token where id = $ACCESS_TOKEN_ID")
    fun getAccessToken(): LiveData<AccessTokenEntity>

    @Query("select * from access_token where id = $ACCESS_TOKEN_ID")
    fun getAccessTokenNonLive(): AccessTokenEntity?
}