package com.threedee.nature.injection.module

import android.app.Application
import androidx.room.Room
import com.threedee.cache.db.NatureDatabase
import com.threedee.cache.implementation.FarmCacheImplementation
import com.threedee.data.repository.farm.FarmCache
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Module that provides all dependencies from the cache layer.
 */
@Module
abstract  class CacheModule {
    @Binds
    abstract fun bindFarmCache(farmCacheImplementation: FarmCacheImplementation): FarmCache

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideNatureDatabase(application: Application): NatureDatabase {
            return NatureDatabase.getInstance(application.applicationContext)
        }
    }
}