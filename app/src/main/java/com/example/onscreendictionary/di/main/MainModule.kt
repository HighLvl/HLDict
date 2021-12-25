package com.example.onscreendictionary.di.main

import com.example.onscreendictionary.ui.base.BaseViewModel
import com.example.onscreendictionary.ui.main.MainViewModel
import com.example.onscreendictionary.ui.search.SearchCoordinator
import com.example.onscreendictionary.ui.search.SearchViewModel
import com.example.onscreendictionary.ui.word_details.WordDetailsCoordinator
import com.example.onscreendictionary.ui.word_details.WordDetailsViewModel
import com.example.onscreendictionary.ui.word_list.WordListCoordinator
import com.example.onscreendictionary.ui.word_list.WordListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(includes = [MainModule.Bindings::class])
class MainModule {
    @Provides
    fun provideSearchCoordinator(impl: MainViewModel): SearchCoordinator {
        return impl.searchCoordinator
    }

    @Provides
    fun provideWordDetailsCoordinator(impl: MainViewModel): WordDetailsCoordinator {
        return impl.wordDetailsCoordinator
    }

    @Provides
    fun provideWordListCoordinator(impl: MainViewModel): WordListCoordinator {
        return impl.wordListCoordinator
    }

    @Module
    interface Bindings {
        @Binds
        @IntoMap
        @ClassKey(MainViewModel::class)
        fun bindMainViewModel(impl: MainViewModel): BaseViewModel

        @Binds
        @IntoMap
        @ClassKey(WordListViewModel::class)
        fun bindWordListViewModel(impl: WordListViewModel): BaseViewModel

        @Binds
        @IntoMap
        @ClassKey(SearchViewModel::class)
        fun bindSearchViewModel(impl: SearchViewModel): BaseViewModel

        @Binds
        @IntoMap
        @ClassKey(WordDetailsViewModel::class)
        fun bindWordDetailsViewModel(impl: WordDetailsViewModel): BaseViewModel
    }
}