package com.yundin.core

import android.content.Context

interface App {
    fun getApplicationContext(): Context
    fun getAppProvider(): ApplicationProvider
}
