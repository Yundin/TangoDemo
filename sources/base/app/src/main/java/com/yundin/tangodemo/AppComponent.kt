package com.yundin.tangodemo

import android.app.Application
import com.yundin.core.ApplicationProvider
import com.yundin.core.dagger.scope.AppScope
import com.yundin.datasource.di.DataSourceModule
import dagger.Component

@[AppScope Component(modules = [DataSourceModule::class], dependencies = [Application::class])]
interface AppComponent : ApplicationProvider
