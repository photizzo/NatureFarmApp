package com.threedee.data.source

import com.threedee.data.repository.farm.FarmDataStore
import javax.inject.Inject

/**
 * Factory to create an instance of a [FarmDataStore]
 */
class FarmDataStoreFactory @Inject constructor(
    private val farmCacheDataSource: FarmCacheDataSource) {

    /**
     * Return an instance of the [FarmDataStore]
     */
    fun retrieveCacheDataStore(): FarmDataStore {
        return farmCacheDataSource
    }
}