package com.example.onscreendictionary.di.app

import com.example.onscreendictionary.di.db.DbModule
import com.example.onscreendictionary.di.main.MainComponent
import com.example.onscreendictionary.di.web.WebModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, WebModule::class, DbModule::class])
interface AppComponent {
    fun mainComponentFactory(): MainComponent.Factory
}