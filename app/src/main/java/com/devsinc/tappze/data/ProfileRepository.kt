package com.devsinc.tappze.data

import com.devsinc.tappze.model.UserData
import com.google.firebase.auth.FirebaseUser

interface ProfileRepository {
    val user: FirebaseUser?
    suspend fun getUserDatabase(): Resource<UserData>
    suspend fun updateUserDatabase(userData: UserData): Resource<String>
    suspend fun updateUserImage(imageUrl: String): Resource<String>
    suspend fun updateUserPassword(password: String): Resource<String>
}