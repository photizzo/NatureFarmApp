package com.threedee.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.threedee.cache.db.DbConstants
import com.threedee.cache.model.CachedFarm
import com.threedee.cache.model.CachedFarmLocation
import com.threedee.cache.model.CachedFarmer
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Provides data access methods for [CachedFarm] and [CachedFarmLocation]
 */
@Dao
abstract class CachedFarmDao {
    /**
     * Get a list of farms
     */
    @Query(DbConstants.QUERY_FARMS)
    abstract fun getFarms(): Single<List<CachedFarm>>

    /**
     * Insert a farm and creates a new row
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFarm(param: CachedFarm): Completable

    /**
     * Insert a farmer and creates a new row
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFarmer(param: CachedFarmer): Completable

    /**
     * Insert a farm location and creates a new row
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertFarmLocation(param: CachedFarmLocation): Completable

    /**
     * Update farmer details
     */
    @Update
    abstract fun updateFarmer(param: CachedFarmer): Completable

    /**
     * Update farm location details
     */
    @Update
    abstract fun updateFarmLocation(param: CachedFarmLocation): Completable

    /**
     * Delete farmer details
     */
    @Update
    abstract fun deleteFarmer(param: CachedFarmer): Completable

    /**
     * Delete farm location details
     */
    @Update
    abstract fun deleteFarmLocation(param: CachedFarmLocation): Completable
}