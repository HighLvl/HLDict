package com.example.onscreendictionary

import android.app.Application
import com.example.onscreendictionary.di.app.AppComponent
import com.example.onscreendictionary.di.app.AppModule
import com.example.onscreendictionary.di.app.DaggerAppComponent

class App : Application() {
    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent get() = _appComponent!!

    override fun onCreate() {
        _appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        super.onCreate()
    }
}