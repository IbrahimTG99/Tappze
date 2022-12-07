package com.devsinc.tappze.model

data class UserData(
    val fullName: String ?= null,
    val about: String ?= null,
    val phone: String ?= null,
    val company: String ?= null,
    val gender: String ?= null,
    val birthDate: String ?= null,
    var infoMap: HashMap<String, String> ?= null,
    var profileStatus: Boolean ?= null,
    var profileImage: String ?= null
)