package com.threedee.cache.implementation

import com.threedee.cache.db.NatureDatabase
import com.threedee.cache.mapper.FarmLocationMapper
import com.threedee.cache.mapper.FarmerMapper
import com.threedee.cache.model.CachedFarm
import com.threedee.data.repository.farm.FarmCache
import com.threedee.domain.model.Farm
import com.threedee.domain.model.FarmLocation
import com.threedee.domain.model.Farmer
import io.reactivex.Completable
import io.reactivex.Single
import java.util.Calendar
import javax.inject.Inject

/**
 * Cached implementation for retrieving and saving Farm instances. This class implements the
 * [FarmCache] from the Data layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out.
 */
class FarmCacheImplementation @Inject constructor(
    private val natureDatabase: NatureDatabase,
    private val farmerMapper: FarmerMapper,
    private val farmLocationMapper: FarmLocationMapper
) : FarmCache {
    override fun addFarm(param: Farm): Completable {
        return natureDatabase.cachedFarmDao().insertFarm(
            CachedFarm(
                farmerMapper.mapToCached(param.farmer),
                farmLocationMapper.mapToCached(param.farmLocation)
            )
        )
    }

    override fun getAllFarms(): Single<List<Farm>> {
        return natureDatabase.cachedFarmDao().getFarms()
            .map { data ->
                println("db data: $data")
                data.map { cachedFarm ->
                    Farm(
                        cachedFarm.id,
                        farmerMapper.mapFromCached(cachedFarm.farmer),
                        farmLocationMapper.mapFromCached(cachedFarm.farmLocation)
                    )
                }
            }
    }

    override fun deleteFarm(param: Farm): Completable {
        println("Delete farm: $param")
        return natureDatabase.cachedFarmDao().deleteFarm(
            CachedFarm(
                farmerMapper.mapToCached(param.farmer),
                farmLocationMapper.mapToCached(param.farmLocation),
                param.id
            )
        )
    }

    override fun updateFarm(param: Farm): Completable {
        println("Update farm: $param")
        return natureDatabase.cachedFarmDao().updateFarm(
            CachedFarm(
                farmerMapper.mapToCached(param.farmer),
                farmLocationMapper.mapToCached(param.farmLocation),
                param.id
            )
        )
    }
}