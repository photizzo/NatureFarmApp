package com.threedee.nature.injection.module

import com.threedee.domain.executor.PostExecutionThread
import com.threedee.nature.UiThread
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Module that provides all dependencies from the mobile-ui package/layer and injects all activities.
 */
@Module
abstract class UiModule {

    @Binds
    abstract fun bindPostExecutionThread(uiThread: UiThread): PostExecutionThread

//    @ContributesAndroidInjector(modules = [AuthModule::class])
//    abstract fun contributeMainActivity(): MainActivity
}