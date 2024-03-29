package com.devsinc.tappze.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val user: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signUp(email: String, password: String, userName: String): Resource<FirebaseUser>
    suspend fun addUserToDatabase(fullName: String)
    suspend fun logout(): Resource<String>
    suspend fun resetPassword(email: String): Resource<String>
    suspend fun updateProfileStatus(status: Boolean): Resource<Boolean>
    suspend fun getProfileStatus(): Resource<Boolean>
}