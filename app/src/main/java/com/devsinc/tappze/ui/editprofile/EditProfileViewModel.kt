package com.devsinc.tappze.ui.editprofile

import android.net.Uri
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

    private val _updateImageFlow = MutableStateFlow<Resource<Uri>?>(null)
    val updateImageFlow = _updateImageFlow

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

    fun updateUserImage(imageUrl: Uri?) = viewModelScope.launch {
        _updateImageFlow.value = Resource.Loading
        val result = repository.updateUserImage(imageUrl!!)
        _updateImageFlow.value = result
    }
}