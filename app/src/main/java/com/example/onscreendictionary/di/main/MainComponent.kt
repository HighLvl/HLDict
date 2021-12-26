package com.example.onscreendictionary.di.main

import com.example.onscreendictionary.di.base.BaseComponent
import dagger.Subcomponent

@MainScope
@Subcomponent(modules = [MainModule::class])
interface MainComponent : BaseComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(module: MainModule): MainComponent
    }
}