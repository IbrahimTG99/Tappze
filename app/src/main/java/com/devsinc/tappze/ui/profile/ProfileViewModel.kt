package com.devsinc.tappze.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.ProfileRepository
import com.devsinc.tappze.data.Resource
import com.devsinc.tappze.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val user = repository.user
    private val _getDatabaseFlow = MutableStateFlow<Resource<UserData>?>(null)
    val getDatabaseFlow = _getDatabaseFlow

    private val _getDatabaseInfoFlow = MutableStateFlow<Resource<HashMap<String, String>>?>(null)
    val getDatabaseInfoFlow = _getDatabaseInfoFlow

    fun getUserDatabase() = viewModelScope.launch {
        _getDatabaseFlow.value = Resource.Loading
        val result = repository.getUserDatabase()
        _getDatabaseFlow.value = result
    }

    fun getUserDatabaseInfo() = viewModelScope.launch {
        _getDatabaseInfoFlow.value = Resource.Loading
        val result = repository.getUserDatabaseInfo()
        _getDatabaseInfoFlow.value = result
    }

}