package ru.kazakovanet.synoptic.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Deferred
import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository
import ru.kazakovanet.synoptic.internal.lazyDeferred

class YahooAPIAuthenticateViewModel(
    private val repository: YahooAuthApiRepository
) : ViewModel() {

    suspend fun changeCodeToAccessToken(code: String) {
        repository.getAccessToken(code)
    }

    suspend fun isAccessTokenReceived(): Boolean {
        return repository.isAccessTokenReceived()
    }
}