package com.example.appioasys

import android.app.Application
import com.example.appioasys.di.appHomeModule
import com.example.appioasys.di.appLoginModule
import com.example.appioasys.di.homeModule
import com.example.appioasys.di.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CompanyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CompanyApp)
            modules(loginModule, homeModule)
            modules(appLoginModule,appHomeModule)
        }
    }
}