package com.threedee.cache.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.threedee.cache.db.Converters
import com.threedee.cache.db.DbConstants

@Entity(
    tableName = DbConstants.FARM_LOCATION_TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = CachedFarmer::class,
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
    @TypeConverters(Converters::class)
    val latitude: List<Double> = listOf(),
    @TypeConverters(Converters::class)
    val longitude: List<Double> = listOf(),
    val farmerId: Int = 0
)