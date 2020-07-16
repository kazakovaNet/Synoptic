package ru.kazakovanet.synoptic.data.repository.auth

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.kazakovanet.synoptic.data.datamapper.AccessTokenDataMapper
import ru.kazakovanet.synoptic.data.db.dao.auth.AccessTokenDao
import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity
import ru.kazakovanet.synoptic.data.network.api.dto.AccessTokenDTO
import ru.kazakovanet.synoptic.data.network.datasource.auth.YahooAuthNetworkDataSource

class YahooAuthApiRepositoryImpl(
    private val dao: AccessTokenDao,
    private val dataSource: YahooAuthNetworkDataSource
) : YahooAuthApiRepository {

    init {
        dataSource.accessToken.observeForever { newAccessToken ->
            persistFetchedAccessToken(newAccessToken)
        }
    }

    override suspend fun getAccessToken(code: String): LiveData<out AccessTokenEntity> {
        return withContext(Dispatchers.IO) {
            initAccessData(code)
            dao.getAccessToken()
        }
    }

    private fun persistFetchedAccessToken(newAccessToken: AccessTokenDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            dao.upsert(AccessTokenDataMapper.map(newAccessToken))
        }
    }

    private suspend fun initAccessData(code: String) {
        val lastAccessToken = dao.getAccessTokenNonLive()

        if (lastAccessToken == null || isFetchAccessTokenNeeded(lastAccessToken.expiresIn)) {
            fetchAccessToken(code)
            return
        }
    }

    private suspend fun fetchAccessToken(code: String) {
        dataSource.fetchAccessToken(code)
    }

    private fun isFetchAccessTokenNeeded(expiresIn: Int): Boolean {
        // TODO: 14.07.2020
        return true
    }

}