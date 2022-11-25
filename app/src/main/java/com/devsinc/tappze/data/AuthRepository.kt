package com.devsinc.tappze.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val user: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun logout()
}