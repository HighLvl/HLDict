package com.example.onscreendictionary.di.db

import android.content.Context
import androidx.room.Room
import com.example.onscreendictionary.db.source.DictionaryDao
import com.example.onscreendictionary.db.source.DictionaryDatabase
import com.example.onscreendictionary.db.source.DictionaryLocalSourceImpl
import com.example.onscreendictionary.domain.local.DictionaryLocalSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DbModule.Bindings::class])
class DbModule {
    @Singleton
    @Provides
    fun provideDictionaryDatabase(context: Context): DictionaryDatabase {
        return Room.databaseBuilder(context, DictionaryDatabase::class.java, "dictionary.db").build()
    }

    @Provides
    fun provideDictionaryDao(impl: DictionaryDatabase): DictionaryDao {
        return impl.dao
    }

    @Module
    interface Bindings {
        @Singleton
        @Binds
        fun bindDictionaryLocalSource(impl: DictionaryLocalSourceImpl): DictionaryLocalSource
    }
}