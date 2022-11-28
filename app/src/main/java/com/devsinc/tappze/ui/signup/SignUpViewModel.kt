package com.devsinc.tappze.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.tappze.data.AuthRepository
import com.devsinc.tappze.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signUpFlow: StateFlow<Resource<FirebaseUser>?> = _signupFlow

    fun signUp(email: String, password: String, userName: String) = viewModelScope.launch {
        _signupFlow.value = Resource.Loading
        val result = repository.signUp(email, password, userName)
        _signupFlow.value = result
    }

    fun addUserToDatabase(fullName: String) = viewModelScope.launch {
        repository.addUserToDatabase(fullName)
    }
}