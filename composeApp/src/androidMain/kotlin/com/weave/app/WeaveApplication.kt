package com.weave.app

import android.app.Application
import com.weave.app.di.initKoin
import org.koin.android.ext.koin.androidContext

class WeaveApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@WeaveApplication)
        }
    }
}
