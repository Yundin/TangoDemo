package com.yundin.tangodemo.di

import android.app.Application
import com.yundin.core.ApplicationProvider
import com.yundin.core.dagger.scope.AppScope
import com.yundin.datasource.di.DataSourceModule
import dagger.BindsInstance
import dagger.Component

@[AppScope Component(
    modules = [
        DataSourceModule::class,
        ContextModule::class,
    ]
)]
interface AppComponent : ApplicationProvider {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
