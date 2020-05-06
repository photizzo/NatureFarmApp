package com.threedee.domain.model

data class Farmer(
    val id: Int,
    val fullName: String,
    val phone: String,
    val avatar: String,
    val email: String?,
    val timeStamp: Long
)