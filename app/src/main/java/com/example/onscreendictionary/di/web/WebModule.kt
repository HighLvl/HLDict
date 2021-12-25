package com.example.onscreendictionary.di.web

import com.example.onscreendictionary.BuildConfig
import com.example.onscreendictionary.domain.remote.DictionaryRemoteSource
import com.example.onscreendictionary.web.source.DictionaryRemoteSourceImpl
import com.example.onscreendictionary.web.source.DictionaryWebInterface
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [WebModule.Bindings::class])
class WebModule {
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return Json.asConverterFactory("application/json".toMediaType())
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        client: OkHttpClient,
        converterFactory: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://3.133.139.98:8000/")
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    @Provides
    fun provideDictionaryWebInterface(retrofit: Retrofit): DictionaryWebInterface {
        return retrofit.create()
    }

    @Module
    interface Bindings {
        @Binds
        fun bindDictionaryRemoteSource(impl: DictionaryRemoteSourceImpl): DictionaryRemoteSource
    }
}