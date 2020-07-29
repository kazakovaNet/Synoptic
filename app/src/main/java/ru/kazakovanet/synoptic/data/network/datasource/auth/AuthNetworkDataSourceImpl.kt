package ru.kazakovanet.synoptic.data.network.datasource.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.GRANT_TYPE
import ru.kazakovanet.synoptic.data.network.api.REDIRECT_URI
import ru.kazakovanet.synoptic.data.network.api.dto.auth.AccessTokenDTO
import ru.kazakovanet.synoptic.data.network.api.service.auth.AuthApiService
import ru.kazakovanet.synoptic.internal.NoConnectivityException

class AuthNetworkDataSourceImpl(
    private val authApiService: AuthApiService
) : AuthNetworkDataSource {

    private val _accessToken = MutableLiveData<AccessTokenDTO>()
    override val accessToken: LiveData<AccessTokenDTO>
        get() = _accessToken

    override suspend fun fetchAccessToken(code: String) {
        try {
            val fieldMap = mapOf(
                Pair("redirect_uri", REDIRECT_URI),
                Pair("grant_type", GRANT_TYPE),
                Pair("code", code)
            )
            val accessToken = authApiService.getAccessToken(fieldMap).await()
            _accessToken.postValue(accessToken)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}