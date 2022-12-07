package com.devsinc.tappze.data

import com.devsinc.tappze.data.utils.Constants
import com.devsinc.tappze.data.utils.await
import com.devsinc.tappze.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : AuthRepository {

    override val user: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Constants.userLoggedIn = firebaseDatabase.child("users").child(result.user?.uid!!)
                .get().await().getValue(UserData::class.java)
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        userName: String
    ): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(userName)
                    .build()
            )?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun addUserToDatabase(fullName: String) {
        val userData = UserData(fullName = fullName, infoMap = hashMapOf())
        Constants.userLoggedIn = userData
        firebaseDatabase.child("users").child(user?.uid!!).setValue(userData).await()
    }

    override suspend fun logout(): Resource<String> {
        return try {
            firebaseAuth.signOut()
            Constants.userLoggedIn = null
            Resource.Success("Logout")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun resetPassword(email: String): Resource<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success("Email sent")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateProfileStatus(status: Boolean): Resource<Boolean> {
        return try {
            if (Constants.userLoggedIn == null) {
                Constants.userLoggedIn = firebaseDatabase.child("users").child(user?.uid!!)
                    .get().await().getValue(UserData::class.java)
            }
            Constants.userLoggedIn!!.profileStatus = status
            firebaseDatabase.child("users").child(user?.uid!!).child("profileStatus")
                .setValue(status).await()
            Resource.Success(status)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getProfileStatus(): Resource<Boolean> {
        return try {
            if (Constants.userLoggedIn == null) {
                Constants.userLoggedIn = firebaseDatabase.child("users").child(user?.uid!!)
                    .get().await().getValue(UserData::class.java)
            }
            if (Constants.userLoggedIn?.profileStatus == null) {
                Constants.userLoggedIn?.profileStatus = false
                firebaseDatabase.child("users").child(user?.uid!!).child("profileStatus")
                    .setValue(Constants.userLoggedIn?.profileStatus).await()
            }
            Resource.Success(Constants.userLoggedIn?.profileStatus!!)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}
