package com.threedee.domain.model

data class Farm(
    val id: Int,
    val farmer: Farmer,
    val farmLocation: FarmLocation
)