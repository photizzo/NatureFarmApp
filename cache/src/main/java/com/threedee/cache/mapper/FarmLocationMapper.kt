package com.threedee.cache.mapper

import com.threedee.cache.model.CachedFarmLocation
import com.threedee.domain.model.FarmLocation
import javax.inject.Inject

/**
 * Map a [CachedFarmLocation] instance to and from a [FarmLocation] instance when data is moving between
 * the Cache and the Data layer
 */
open class FarmLocationMapper @Inject constructor() :
    EntityMapper<CachedFarmLocation, FarmLocation> {
    override fun mapFromCached(type: CachedFarmLocation): FarmLocation {
        return FarmLocation(type.id, type.name, type.address, type.latitude, type.longitude)
    }

    override fun mapToCached(type: FarmLocation): CachedFarmLocation {
        return CachedFarmLocation(type.id, type.name, type.address, type.latitude, type.longitude)
    }
}