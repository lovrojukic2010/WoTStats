package com.example.wotstats

import android.app.Application
import com.example.wotstats.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WotStatsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WotStatsApp)
            modules(viewModelModule)
        }
    }
}