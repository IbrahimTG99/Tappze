package com.devsinc.tappze.di

import com.devsinc.tappze.data.AuthRepository
import com.devsinc.tappze.data.AuthRepositoryImpl
import com.devsinc.tappze.data.ProfileRepository
import com.devsinc.tappze.data.ProfileRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideFirebaseDatabase(): DatabaseReference = Firebase.database.reference

    @Provides
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository = impl

    @Provides
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl

    @Provides
    fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference
}