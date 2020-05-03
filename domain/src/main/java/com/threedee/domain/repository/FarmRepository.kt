package com.threedee.domain.repository

import com.threedee.domain.model.Farm
import io.reactivex.Completable
import io.reactivex.Single

interface FarmRepository {
    fun addFarm(param: Farm): Completable
    fun getAllFarms(): Single<List<Farm>>
    fun deleteFarm(param: Farm): Completable
    fun updateFarm(param: Farm): Completable
}