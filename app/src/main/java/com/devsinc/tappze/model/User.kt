package com.devsinc.tappze.model

import android.net.Uri

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val infoMap: MutableMap<String, Uri>,
    val created_at: String,
    val updated_at: String
)
