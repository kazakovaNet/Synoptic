package ru.kazakovanet.synoptic.data.repository.auth

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity

/**
 * Created by NKazakova on 14.07.2020.
 */
interface YahooAuthApiRepository {
    suspend fun getAccessToken(code: String): Boolean
    suspend fun isAccessTokenReceived(): Boolean
}