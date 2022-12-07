package com.devsinc.tappze.data

import android.net.Uri
import com.devsinc.tappze.model.UserData
import com.google.firebase.auth.FirebaseUser

interface ProfileRepository {
    val user: FirebaseUser?
    suspend fun getUserDatabase(): Resource<UserData>
    suspend fun getUserDatabaseInfo(): Resource<HashMap<String, String>>
    suspend fun updateUserDatabase(userData: UserData): Resource<String>
    suspend fun updateUserDatabaseInfo(appName: String, appUrl: String): Resource<String>
    suspend fun updateUserImage(imageLocalUri: Uri): Resource<Uri>
    suspend fun updateUserPassword(password: String): Resource<String>
    suspend fun deleteUserDatabaseInfo(appName: String): Resource<String>
}