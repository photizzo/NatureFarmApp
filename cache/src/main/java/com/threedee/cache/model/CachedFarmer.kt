package com.threedee.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.threedee.cache.db.DbConstants

@Entity(tableName = DbConstants.FARMER_TABLE_NAME)
class CachedFarmer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String = "",
    val phone: String = "",
    val avatar: String = "",
    val email: String? = null
)