package com.yundin.tangodemo.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module

@Module
interface ContextModule {
    @Binds
    fun context(app: Application): Context
}
