package com.threedee.cache.mapper

import com.threedee.cache.model.CachedFarmer
import com.threedee.domain.model.Farmer
import javax.inject.Inject

/**
 * Map a [CachedFarmer] instance to and from a [Farmer] instance when data is moving between
 * the Cache and the Data layer
 */
open class FarmerMapper @Inject constructor() : EntityMapper<CachedFarmer, Farmer> {
    override fun mapFromCached(type: CachedFarmer): Farmer {
        return Farmer(type.id, type.fullName, type.phone, type.avatar, type.email)
    }

    override fun mapToCached(type: Farmer): CachedFarmer {
        return CachedFarmer(type.id, type.fullName, type.phone, type.avatar, type.email)
    }
}