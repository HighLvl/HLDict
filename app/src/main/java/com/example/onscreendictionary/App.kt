package com.example.onscreendictionary

import android.app.Application
import com.example.onscreendictionary.di.app.AppComponent
import com.example.onscreendictionary.di.app.AppModule
import com.example.onscreendictionary.di.app.DaggerAppComponent
import com.example.onscreendictionary.di.db.DbModule

class App : Application() {
    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent get() = _appComponent!!

    override fun onCreate() {
        _appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .dbModule(DbModule())
            .build()

        super.onCreate()
    }
}