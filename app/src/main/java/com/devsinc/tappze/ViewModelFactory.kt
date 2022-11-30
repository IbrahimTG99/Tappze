//package com.devsinc.tappze
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.devsinc.tappze.data.AuthRepository
//import com.devsinc.tappze.data.AuthRepositoryImpl
//import com.devsinc.tappze.ui.settings.SettingsViewModel
//import com.devsinc.tappze.ui.signin.SignInViewModel
//import com.devsinc.tappze.ui.signup.SignUpViewModel
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//
//class ViewModelFactory: ViewModelProvider.Factory {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T = when(modelClass){
//        SignInViewModel::class.java -> SignInViewModel(AuthRepositoryImpl(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()))
//        SignUpViewModel::class.java -> SignUpViewModel(AuthRepositoryImpl(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()))
//        SettingsViewModel::class.java -> SettingsViewModel(AuthRepositoryImpl(FirebaseAuth.getInstance(), FirebaseDatabase.getInstance()))
//        else -> throw IllegalArgumentException("Unknown ViewModel class")
//    } as T
//
//}