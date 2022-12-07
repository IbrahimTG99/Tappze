package com.devsinc.tappze.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.AuthRepository
import com.devsinc.tappze.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _logoutFlow = MutableStateFlow<Resource<String>?>(null)
    val getDatabaseFlow = _logoutFlow

    private val _getStatusFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val getStatusFlow = _getStatusFlow

    private val _setStatusFlow = MutableStateFlow<Resource<Boolean>?>(null)
    val setStatusFlow = _setStatusFlow

    fun updateProfileStatus(status: Boolean) = viewModelScope.launch {
        _setStatusFlow.value = Resource.Loading
        _setStatusFlow.value = repository.updateProfileStatus(status)
    }

    fun getProfileStatus() = viewModelScope.launch {
        _getStatusFlow.value = Resource.Loading
        _getStatusFlow.value = repository.getProfileStatus()
    }
    fun logout() = viewModelScope.launch {
        _logoutFlow.value = Resource.Loading
        val result = repository.logout()
        _logoutFlow.value = result
    }
}