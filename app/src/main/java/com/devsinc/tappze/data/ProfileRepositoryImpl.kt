package com.devsinc.tappze.data

import android.net.Uri
import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.data.utils.await
import com.devsinc.tappze.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference,
    private val firebaseStorage: StorageReference
) : ProfileRepository {

    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun getUserDatabase(): Resource<UserData> {
        if (Constants.userLoggedIn != null) {
            return Resource.Success(Constants.userLoggedIn!!)
        }
        return try {
            val result = firebaseDatabase.child("users").child(user?.uid!!).get().await()
            val userData = result.getValue(UserData::class.java)
            Constants.userLoggedIn = userData
            Resource.Success(userData!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getUserDatabaseInfo(): Resource<HashMap<String, String>> {
        return try {
            if (Constants.userLoggedIn == null) {
                Constants.userLoggedIn = firebaseDatabase.child("users").child(user?.uid!!)
                    .get().await().getValue(UserData::class.java)
            }
            if (Constants.userLoggedIn!!.infoMap == null) {
                Constants.userLoggedIn!!.infoMap = HashMap<String, String>()
            }
            Resource.Success(Constants.userLoggedIn!!.infoMap!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserDatabase(userData: UserData): Resource<String> {
        return try {
            Constants.userLoggedIn = userData
            firebaseDatabase.child("users").child(user?.uid!!).setValue(userData).await()
            Resource.Success("User updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateUserImage(imageLocalUri: Uri): Resource<Uri> {
        return try {
            val downloadUrl =
                firebaseStorage.child("images").child(user?.uid!!).putFile(imageLocalUri)
                    .await().storage.downloadUrl.await()
            Resource.Success(downloadUrl)
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
            if (Constants.userLoggedIn == null) {
                Constants.userLoggedIn = firebaseDatabase.child("users").child(user?.uid!!)
                    .get().await().getValue(UserData::class.java)
            }
            if (Constants.userLoggedIn!!.infoMap == null) {
                Constants.userLoggedIn!!.infoMap = HashMap<String, String>()
            }
            Constants.userLoggedIn!!.infoMap!![appName] = appUrl
            firebaseDatabase.child("users").child(user?.uid!!).child("infoMap")
                .setValue(Constants.userLoggedIn!!.infoMap).await()
            Resource.Success("Info updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun deleteUserDatabaseInfo(appName: String): Resource<String> {
        return try {
            if (Constants.userLoggedIn == null) {
                Constants.userLoggedIn = firebaseDatabase.child("users").child(user?.uid!!)
                    .get().await().getValue(UserData::class.java)
            }
            if (Constants.userLoggedIn!!.infoMap == null) {
                Constants.userLoggedIn!!.infoMap = HashMap<String, String>()
            }
            Constants.userLoggedIn!!.infoMap!!.remove(appName)
            firebaseDatabase.child("users").child(user?.uid!!).child("infoMap")
                .setValue(Constants.userLoggedIn!!.infoMap).await()
            Resource.Success("Info deleted")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}