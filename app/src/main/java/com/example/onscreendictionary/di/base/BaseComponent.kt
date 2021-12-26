package com.example.onscreendictionary.di.base

import com.example.onscreendictionary.ui.base.BaseViewModel
import javax.inject.Provider

interface BaseComponent {
    fun viewModelProviders(): @JvmSuppressWildcards Map<Class<*>, Provider<BaseViewModel>>
}