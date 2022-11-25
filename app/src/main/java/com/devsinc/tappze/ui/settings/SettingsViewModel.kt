package com.devsinc.tappze.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.AuthRepository
import com.devsinc.tappze.data.Resource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)

    init {
        if(repository.user != null) {
            _loginFlow.value = Resource.Success(repository.user!!)
        }
    }

    fun logout() = viewModelScope.launch {
        repository.logout()
        _loginFlow.value = null
    }
}