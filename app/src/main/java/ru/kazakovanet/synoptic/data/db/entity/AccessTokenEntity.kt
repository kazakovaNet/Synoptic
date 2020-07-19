package ru.kazakovanet.synoptic.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by NKazakova on 14.07.2020.
 */
const val ACCESS_TOKEN_ID = 0

@Entity(tableName = "access_token")
class AccessTokenEntity(
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "token_type")
    val tokenType: String,
    @ColumnInfo(name = "expires_in")
    val expiresDate: Long,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String,
    @ColumnInfo(name = "xoauth_yahoo_guid")
    val xOauthYahooGuid: String?
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = ACCESS_TOKEN_ID
}