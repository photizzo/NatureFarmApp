package com.threedee.nature.injection

import android.app.Application
import com.threedee.nature.App
import com.threedee.nature.injection.module.ApplicationModule
import com.threedee.nature.injection.module.CacheModule
import com.threedee.nature.injection.module.DataModule
import com.threedee.nature.injection.module.PresentationModule
import com.threedee.nature.injection.module.UiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        UiModule::class,
        PresentationModule::class,
        DataModule::class,
        CacheModule::class]
)
interface ApplicationComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun create(app: Application): Builder

        fun build(): ApplicationComponent
    }
}