package com.devsinc.tappze.ui.editinfo

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
class EditInfoViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val user = repository.user
    private val _updateDatabaseFlow = MutableStateFlow<Resource<String>?>(null)
    val updateDatabaseFlow = _updateDatabaseFlow

    private val _getDatabaseInfoFlow = MutableStateFlow<Resource<HashMap<String, String>>?>(null)
    val getDatabaseInfoFlow = _getDatabaseInfoFlow

    fun updateUserDatabaseInfo(appName: String, appUrl: String) = viewModelScope.launch {
        _updateDatabaseFlow.value = Resource.Loading
        val result = repository.updateUserDatabaseInfo(appName, appUrl)
        _updateDatabaseFlow.value = result
    }

    fun getUserDatabaseInfo() = viewModelScope.launch {
        _getDatabaseInfoFlow.value = Resource.Loading
        val result = repository.getUserDatabaseInfo()
        _getDatabaseInfoFlow.value = result
    }

}