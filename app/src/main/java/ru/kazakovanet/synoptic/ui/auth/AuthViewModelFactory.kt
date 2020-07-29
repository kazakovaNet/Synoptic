package ru.kazakovanet.synoptic.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.repository.auth.AuthApiRepository

class AuthViewModelFactory(
    private val repository: AuthApiRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}
