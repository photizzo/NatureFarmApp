package com.threedee.nature.injection.module

import com.threedee.data.JobExecutor
import com.threedee.data.implementation.FarmDataRepository
import com.threedee.domain.executor.ThreadExecutor
import com.threedee.domain.repository.FarmRepository
import dagger.Binds
import dagger.Module

/**
 * Module used to provide dependencies at data level.
 */
@Module
abstract class DataModule {
    @Binds
    abstract fun bindThreadExecutor(jobExecutor: JobExecutor): ThreadExecutor

    @Binds
    abstract fun bindBufferooRepository(farmDataRepository: FarmDataRepository): FarmRepository
}