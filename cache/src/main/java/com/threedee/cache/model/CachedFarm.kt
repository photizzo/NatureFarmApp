package com.threedee.cache.model

import androidx.room.Embedded
import androidx.room.Relation

data class CachedFarm(
    @Embedded
    val farmer: CachedFarmer,
    @Relation(
        parentColumn = "id",
        entityColumn = "farmerId"
    )
    val farmLocation: CachedFarmLocation
)