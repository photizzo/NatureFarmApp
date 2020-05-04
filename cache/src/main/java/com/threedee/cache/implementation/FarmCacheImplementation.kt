package com.threedee.cache.implementation

import com.threedee.cache.db.NatureDatabase
import com.threedee.cache.mapper.FarmLocationMapper
import com.threedee.cache.mapper.FarmerMapper
import com.threedee.data.repository.farm.FarmCache
import com.threedee.domain.model.Farm
import io.reactivex.Completable
import io.reactivex.Single
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
        return natureDatabase.cachedFarmDao().insertFarmer(farmerMapper.mapToCached(param.farmer))
            .concatWith(
                natureDatabase.cachedFarmDao().insertFarmLocation(
                    farmLocationMapper.mapToCached(
                        param.farmLocation
                    )
                )
            )
    }

    override fun getAllFarms(): Single<List<Farm>> {
        return natureDatabase.cachedFarmDao().getFarms()
            .map { data ->
                data.map { cachedFarm ->
                    Farm(
                        farmerMapper.mapFromCached(cachedFarm.farmer),
                        farmLocationMapper.mapFromCached(cachedFarm.farmLocation)
                    )
                }
            }
    }

    override fun deleteFarm(param: Farm): Completable {
        return natureDatabase.cachedFarmDao()
            .deleteFarmLocation(farmLocationMapper.mapToCached(param.farmLocation))
            .concatWith(natureDatabase.cachedFarmDao().deleteFarmer(farmerMapper.mapToCached(param.farmer)))
    }

    override fun updateFarm(param: Farm): Completable {
        return natureDatabase.cachedFarmDao()
            .updateFarmLocation(farmLocationMapper.mapToCached(param.farmLocation))
            .concatWith(natureDatabase.cachedFarmDao().updateFarmer(farmerMapper.mapToCached(param.farmer)))
    }
}