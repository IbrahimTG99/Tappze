package com.devsinc.tappze.data

import android.net.Uri
import com.devsinc.tappze.data.utils.await
import com.devsinc.tappze.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : ProfileRepository {

    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun getUserDatabase(): Resource<UserData> {
        return try {
            val result = firebaseDatabase.child("users").child(user?.uid!!).get().await()
            val userData = result.getValue(UserData::class.java)
            Resource.Success(userData!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getUserDatabaseInfo(): Resource<HashMap<String, String>> {
        return try {
            var infoMap = firebaseDatabase.child("users").child(user?.uid!!).child("infoMap").get()
                .await().value as HashMap<String, String>?
            if (infoMap == null) {
                infoMap = HashMap<String, String>()
            }
            Resource.Success(infoMap)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserDatabase(userData: UserData): Resource<String> {
        return try {
            firebaseDatabase.child("users").child(user?.uid!!).setValue(userData).await()
            Resource.Success("User updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserImage(imageUrl: String): Resource<String> {
        return try {
            user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(imageUrl))
                    .build()
            )?.await()
            Resource.Success("Image updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserPassword(password: String): Resource<String> {
        return try {
            user?.updatePassword(password)?.await()
            Resource.Success("Password updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserDatabaseInfo(appName: String, appUrl: String): Resource<String> {
        return try {
            var infoMap = firebaseDatabase.child("users").child(user?.uid!!).child("infoMap").get().await().value as HashMap<String, String>?
            if (infoMap == null) {
                infoMap = HashMap<String, String>()
            }
            infoMap[appName] = appUrl
            firebaseDatabase.child("users").child(user?.uid!!).child("infoMap").setValue(infoMap).await()

            Resource.Success("Info updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}