package com.devsinc.tappze.ui.editprofile

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
class EditProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    val user = repository.user
    private val _updateDatabaseFlow = MutableStateFlow<Resource<String>?>(null)
    val updateDatabaseFlow = _updateDatabaseFlow

    private val _getDatabaseFlow = MutableStateFlow<Resource<UserData>?>(null)
    val getDatabaseFlow = _getDatabaseFlow

    fun getUserDatabase() = viewModelScope.launch {
        _getDatabaseFlow.value = Resource.Loading
        val result = repository.getUserDatabase()
        _getDatabaseFlow.value = result
    }

    fun updateUserDatabase(userData: UserData) = viewModelScope.launch {
        _updateDatabaseFlow.value = Resource.Loading
        val result = repository.updateUserDatabase(userData)
        _updateDatabaseFlow.value = result
    }

    fun updateUserImage(imageUrl: String) = viewModelScope.launch {
        _updateDatabaseFlow.value = Resource.Loading
        val result = repository.updateUserImage(imageUrl)
        _updateDatabaseFlow.value = result
    }
}