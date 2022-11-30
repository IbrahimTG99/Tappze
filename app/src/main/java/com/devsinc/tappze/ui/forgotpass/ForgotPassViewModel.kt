package com.devsinc.tappze.ui.forgotpass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.AuthRepository
import com.devsinc.tappze.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPassViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _resetFlow = MutableStateFlow<Resource<String>?>(null)
    val resetFlow: StateFlow<Resource<String>?> = _resetFlow

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetFlow.value = Resource.Loading
        val result = repository.resetPassword(email)
        _resetFlow.value = result
    }
}
