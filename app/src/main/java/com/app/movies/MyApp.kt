package com.app.movies

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.app.movies.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        //KOIN Dependency Injection
        startKoin {
            androidContext(this@MyApp)
            modules(listOf(AppModule.networkModule))
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}