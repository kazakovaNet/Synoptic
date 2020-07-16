package ru.kazakovanet.synoptic.data.network.datasource.auth

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.network.api.dto.AccessTokenDTO

/**
 * Created by NKazakova on 14.07.2020.
 */
interface YahooAuthNetworkDataSource {
    val accessToken: LiveData<AccessTokenDTO>

    suspend fun fetchAccessToken(code: String)
}