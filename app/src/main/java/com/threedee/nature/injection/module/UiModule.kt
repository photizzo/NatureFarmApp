package com.threedee.nature.injection.module

import com.threedee.domain.executor.PostExecutionThread
import com.threedee.nature.UiThread
import com.threedee.nature.add.AddFarmActivity
import com.threedee.nature.add.AddFarmLocationDetailsFragment
import com.threedee.nature.add.AddFarmersFragment
import com.threedee.nature.home.FarmDetailsActivity
import com.threedee.nature.home.MainActivity
import com.threedee.nature.login.LoginActivity
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

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeFarmDetailActivity(): FarmDetailsActivity

    @ContributesAndroidInjector
    abstract fun contributeAddFarmActivity(): AddFarmActivity

    @ContributesAndroidInjector
    abstract fun contributeAddFarmerFragment(): AddFarmersFragment

    @ContributesAndroidInjector
    abstract fun contributeAddFarmLocaitonFragment(): AddFarmLocationDetailsFragment
}