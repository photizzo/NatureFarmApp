package com.threedee.data.source

import com.threedee.data.repository.farm.FarmCache
import com.threedee.data.repository.farm.FarmDataStore
import com.threedee.domain.model.Farm
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Implementation of the [FarmDataStore] interface to provide a means of communicating
 * with the local data source
 */
class FarmCacheDataSource @Inject constructor(
    private val farmCache: FarmCache): FarmDataStore {
    override fun addFarm(param: Farm): Completable {
        return farmCache.addFarm(param)
    }

    override fun getAllFarms(): Single<List<Farm>> {
        return farmCache.getAllFarms()
    }

    override fun deleteFarm(param: Farm): Completable {
        return farmCache.deleteFarm(param)
    }

    override fun updateFarm(param: Farm): Completable {
        return farmCache.updateFarm(param)
    }
}