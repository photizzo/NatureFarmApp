package com.threedee.cache.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.threedee.cache.db.DbConstants

@Entity(
    tableName = DbConstants.FARM_LOCATION_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CachedFarm::class,
            parentColumns = ["id"],
            childColumns = ["farmerId"]
        )
    ]
)
class CachedFarmLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val address: String = "",
    val latitude: List<Double> = listOf(),
    val longitude: List<Double> = listOf(),
    val farmerId: Int = 0
)