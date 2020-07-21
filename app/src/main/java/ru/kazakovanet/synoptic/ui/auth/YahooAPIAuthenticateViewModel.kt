package ru.kazakovanet.synoptic.ui.auth

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository

class YahooAPIAuthenticateViewModel(
    private val repository: YahooAuthApiRepository
) : ViewModel() {

    suspend fun changeCodeToAccessToken(code: String) {
        repository.getAccessToken(code)
    }
}