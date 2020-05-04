package com.threedee.nature

import com.threedee.nature.injection.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .create(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }

    /**
     * Set up timber for logging
     */
    private fun setupTimber() {
        Timber.plant(Timber.DebugTree())
    }
}