package com.example.onscreendictionary.di.app

import android.content.Context
import com.example.onscreendictionary.di.main.MainComponent
import com.example.onscreendictionary.domain.repository.DictionaryRepository
import com.example.onscreendictionary.domain.repository.DictionaryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(
    includes = [AppModule.Bindings::class],
    subcomponents = [MainComponent::class]
)
class AppModule(
    @get:Provides
    val context: Context
) {
    @Module
    interface Bindings {
        @Binds
        fun provideDictionaryRepository(impl: DictionaryRepositoryImpl): DictionaryRepository
    }
}