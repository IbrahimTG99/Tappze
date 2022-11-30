package com.devsinc.tappze.model

import android.net.Uri

data class UserData(
    val uid: String ?= null,
    val fullName: String ?= null,
    val infoMap: MutableMap<String, Uri> ?= null
)