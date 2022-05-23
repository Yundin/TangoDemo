package com.yundin.tangodemo

import android.app.Application
import com.yundin.core.App
import com.yundin.core.ApplicationProvider
import com.yundin.tangodemo.di.AppComponent
import com.yundin.tangodemo.di.DaggerAppComponent

class AppImpl : Application(), App {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun getAppProvider(): ApplicationProvider = appComponent
}
