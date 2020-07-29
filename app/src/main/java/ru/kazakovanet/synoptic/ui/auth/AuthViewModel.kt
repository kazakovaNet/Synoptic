package ru.kazakovanet.synoptic.ui.auth

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.repository.auth.AuthApiRepository

class AuthViewModel(
    private val repository: AuthApiRepository
) : ViewModel() {

    suspend fun changeCodeToAccessToken(code: String) {
        repository.getAccessToken(code)
    }
}