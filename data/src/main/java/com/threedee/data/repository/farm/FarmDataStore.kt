package com.threedee.data.repository.farm

import com.threedee.domain.model.Farm
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for the data operations related to [Farm].
 * This is to be implemented by external data source layers, setting the requirements for the
 * operations that need to be implemented
 */
interface FarmDataStore {
    /**
     * Adding a farm
     */
    fun addFarm(param: Farm): Completable

    /**
     * Getting list of farms
     */
    fun getAllFarms(): Single<List<Farm>>

    /**
     * Deleting a farm
     */
    fun deleteFarm(param: Farm): Completable

    /**
     * Updating a farm
     */
    fun updateFarm(param: Farm): Completable
}