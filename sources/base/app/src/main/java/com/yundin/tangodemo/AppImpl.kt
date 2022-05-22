package com.yundin.tangodemo

import android.app.Application
import com.yundin.core.App
import com.yundin.core.ApplicationProvider

class AppImpl : Application(), App {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun getAppProvider(): ApplicationProvider = appComponent
}
