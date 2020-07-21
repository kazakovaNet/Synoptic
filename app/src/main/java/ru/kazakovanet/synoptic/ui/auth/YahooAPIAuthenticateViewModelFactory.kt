package ru.kazakovanet.synoptic.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository

class YahooAPIAuthenticateViewModelFactory(
    private val repository: YahooAuthApiRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return YahooAPIAuthenticateViewModel(repository) as T
    }
}
