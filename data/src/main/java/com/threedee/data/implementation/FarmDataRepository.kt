package com.threedee.data.implementation

import com.threedee.data.source.FarmDataStoreFactory
import com.threedee.domain.model.Farm
import com.threedee.domain.repository.FarmRepository
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

/**
 * Provides an implementation of the [FarmRepository] interface for communicating to and from
 * data sources
 */
class FarmDataRepository @Inject constructor(
    private val factory: FarmDataStoreFactory
): FarmRepository {
    override fun addFarm(param: Farm): Completable {
        return factory.retrieveCacheDataStore().addFarm(param)
    }

    override fun getAllFarms(): Single<List<Farm>> {
        return factory.retrieveCacheDataStore().getAllFarms()
    }

    override fun deleteFarm(param: Farm): Completable {
        return factory.retrieveCacheDataStore().deleteFarm(param)
    }

    override fun updateFarm(param: Farm): Completable {
        return factory.retrieveCacheDataStore().updateFarm(param)
    }
}