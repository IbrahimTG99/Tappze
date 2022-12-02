package com.devsinc.tappze.model

data class UserData(
    val fullName: String ?= null,
    val about: String ?= null,
    val phone: String ?= null,
    val company: String ?= null,
    val gender: String ?= null,
    val birthDate: String ?= null,
    val infoMap: MutableMap<String, String> ?= null,
    val profileStatus: Boolean ?= null
)