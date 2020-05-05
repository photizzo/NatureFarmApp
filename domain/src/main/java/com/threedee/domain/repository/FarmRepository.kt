package com.threedee.domain.repository

import com.threedee.domain.interactor.farm.LoginUser
import com.threedee.domain.model.Farm
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Interface defining methods for how the data layer can pass data to and from the Domain layer.
 * This is to be implemented by the data layer, setting the requirements for the
 * operations that need to be implemented
 */
interface FarmRepository {
    /**
     * Login a user
     */
    fun loginUser(param: LoginUser.Params): Completable
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