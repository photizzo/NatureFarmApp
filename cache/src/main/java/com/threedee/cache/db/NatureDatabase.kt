package com.threedee.cache.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.threedee.cache.dao.CachedFarmDao
import com.threedee.cache.model.CachedFarm
import com.threedee.cache.model.CachedFarmLocation
import com.threedee.cache.model.CachedFarmer
import javax.inject.Inject

/**
 * Provides the database instance
 */
@Database(
    entities = [CachedFarmer::class, CachedFarmLocation::class, CachedFarm::class],
    exportSchema = true,
    version = 1
)
@TypeConverters(Converters::class)
abstract class NatureDatabase @Inject constructor() : RoomDatabase() {

    abstract fun cachedFarmDao(): CachedFarmDao

    companion object {
        private var INSTANCE: NatureDatabase? = null

        private val sLock = Any()

        /**
         * Util method to get instance of nature db
         */
        fun getInstance(context: Context): NatureDatabase {
            if (INSTANCE == null) {
                synchronized(sLock) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            NatureDatabase::class.java, "nature.db"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                    return INSTANCE!!
                }
            }
            return INSTANCE!!
        }
    }
}