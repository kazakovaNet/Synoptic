package ru.kazakovanet.synoptic.data.repository.auth

/**
 * Created by NKazakova on 14.07.2020.
 */
interface AuthApiRepository {
    suspend fun getAccessToken(code: String)
    suspend fun isAccessTokenReceived(): Boolean
}