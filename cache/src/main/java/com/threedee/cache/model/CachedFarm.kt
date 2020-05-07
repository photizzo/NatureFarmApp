package com.threedee.cache.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.threedee.cache.db.DbConstants.FARM_TABLE_NAME

@Entity(tableName = FARM_TABLE_NAME)
data class CachedFarm(
    @Embedded(prefix = "cf_")
    val farmer: CachedFarmer,
    @Embedded(prefix = "cfl_")
    val farmLocation: CachedFarmLocation,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)