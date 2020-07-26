package ru.kazakovanet.synoptic.data.repository.auth

/**
 * Created by NKazakova on 14.07.2020.
 */
interface YahooAuthApiRepository {
    suspend fun getAccessToken(code: String)
    suspend fun isAccessTokenReceived(): Boolean
}