package com.threedee.domain.model

data class FarmLocation(
    val id: Int,
    val name: String,
    val address: String,
    val latitude: List<Double>,
    val longitude: List<Double>
)