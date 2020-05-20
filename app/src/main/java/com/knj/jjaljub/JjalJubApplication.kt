package com.knj.jjaljub

import android.app.Application
import com.knj.jjaljub.di.jjalCreateViewModelModule
import com.knj.jjaljub.di.roomModule
import com.knj.jjaljub.di.viewModelModule
import org.koin.android.ext.android.startKoin

class JjalJubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(applicationContext, listOf(viewModelModule, jjalCreateViewModelModule, roomModule))
    }
}