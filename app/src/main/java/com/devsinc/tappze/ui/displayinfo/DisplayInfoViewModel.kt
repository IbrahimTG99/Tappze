package com.devsinc.tappze.ui.displayinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.ProfileRepository
import com.devsinc.tappze.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayInfoViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _getDatabaseInfoFlow = MutableStateFlow<Resource<HashMap<String, String>>?>(null)
    val getDatabaseInfoFlow = _getDatabaseInfoFlow


    fun getUserDatabaseInfo() = viewModelScope.launch {
        _getDatabaseInfoFlow.value = Resource.Loading
        val result = repository.getUserDatabaseInfo()
        _getDatabaseInfoFlow.value = result
    }

}