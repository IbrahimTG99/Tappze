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

    fun logout() = viewModelScope.launch {
        _logoutFlow.value = Resource.Loading
        val result = repository.logout()
        _logoutFlow.value = result
    }
}