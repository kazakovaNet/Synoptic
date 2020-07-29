package ru.kazakovanet.synoptic.data.repository.auth

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kazakovanet.synoptic.data.datamapper.AccessTokenDataMapper
import ru.kazakovanet.synoptic.data.db.dao.auth.AccessTokenDao
import ru.kazakovanet.synoptic.data.network.api.dto.auth.AccessTokenDTO
import ru.kazakovanet.synoptic.data.network.datasource.auth.AuthNetworkDataSource

class AuthApiRepositoryImpl(
    private val dao: AccessTokenDao,
    private val dataSource: AuthNetworkDataSource
) : AuthApiRepository {

    init {
        dataSource.accessToken.observeForever { newAccessToken ->
            Log.d("Yahoo", "Token:  ${newAccessToken.accessToken}")
            persistAccessToken(newAccessToken)
        }
    }

    override suspend fun getAccessToken(code: String) {
        return withContext(Dispatchers.IO) {
            initAccessData(code)
        }
    }

    override suspend fun isAccessTokenReceived(): Boolean {
        return withContext(Dispatchers.IO) {
            val lastAccessToken = dao.getAccessTokenNonLive() ?: return@withContext false
            !isFetchAccessTokenNeeded(lastAccessToken.expiresDate)
        }
    }

    private fun persistAccessToken(newAccessToken: AccessTokenDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.upsert(AccessTokenDataMapper.map(newAccessToken))
        }
    }

    private suspend fun initAccessData(code: String) {
        val lastAccessToken = dao.getAccessTokenNonLive()

        if (lastAccessToken == null || isFetchAccessTokenNeeded(lastAccessToken.expiresDate)) {
            dataSource.fetchAccessToken(code)
        }
    }

    private fun isFetchAccessTokenNeeded(expiresIn: Long): Boolean {
        return System.currentTimeMillis() / 1000 <= expiresIn
    }
}