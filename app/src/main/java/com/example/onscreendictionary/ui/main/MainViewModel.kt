package com.example.onscreendictionary.ui.main

import androidx.lifecycle.MutableLiveData
import com.example.onscreendictionary.di.main.MainScope
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.ui.base.BaseViewModel
import com.example.onscreendictionary.ui.search.SearchCoordinator
import com.example.onscreendictionary.ui.word_details.WordDetailsCoordinator
import com.example.onscreendictionary.ui.word_list.WordListCoordinator
import javax.inject.Inject

@MainScope
class MainViewModel @Inject constructor(
) : BaseViewModel() {
    override val args: MainArgs get() = args()
    lateinit var coordinator: MainCoordinator
    private val sections = mutableMapOf<MainBottomSection, MutableList<MainRoute>>(
        MainBottomSection.Search to mutableListOf(MainRoute.WordList()),
        //MainBottomSection.Reminder to mutableListOf(MainRoute.Reminder),
        MainBottomSection.Favorites to mutableListOf(MainRoute.FavoriteList())
    )
    val section: MutableLiveData<Pair<MainBottomSection, MainRoute>> =
        MutableLiveData(MainBottomSection.Search to MainRoute.WordList())

    fun search(query: String) {
        addRoute(MainBottomSection.Search, MainRoute.Search(query))
    }

    private fun backImpl(commit: Boolean = true) {
        val section = section.value!!.first
        val backStack = sections.getValue(section)
        if (backStack.isNotEmpty()) {
            backStack.removeLast()
            if (commit) {
                if (backStack.isNotEmpty()) {
                    updateSectionRoute(section)
                } else {
                    coordinator.back()
                }
            }
        } else {
            if (commit) {
                coordinator.back()
            }
        }
    }

    private fun addRoute(
        section: MainBottomSection = this.section.value!!.first,
        route: MainRoute,
        commit: Boolean = true
    ) {
        if (sections.getValue(section).lastOrNull() != route) {
            sections.getValue(section).add(route)
            if (commit) {
                updateSectionRoute(section)
            }
        }
    }

    private fun updateSectionRoute(section: MainBottomSection) {
        val route = sections.getValue(section).last()
        this.section.value = section to route
    }

    private var isCreated = false
    fun onCreate(instanceState: MainInstanceState?) {
        if (isCreated) {
            return
        }
        isCreated = true
        when {
            instanceState != null -> {
                sections.clear()
                sections.putAll(instanceState.sections)
                section.value = instanceState.sectionKey to instanceState.sectionValue
            }
            args.query != null -> addRoute(MainBottomSection.Search, MainRoute.Search(args.query))
        }
    }

    fun onSaveInstanceState(): MainInstanceState {
        return MainInstanceState(sections, section.value!!.first, section.value!!.second)
    }

    fun onBottomSectionClick(section: MainBottomSection) {
        if (section != this.section.value!!.first) {
            updateSectionRoute(section)
        }
    }

    val searchCoordinator = object : SearchCoordinator {
        override fun wordList(query: String) {
            val newRoute = if (section.value!!.second is MainRoute.Search) {
                MainRoute.WordList(query)
            } else {
                MainRoute.FavoriteList(query)
            }
            backImpl(commit = false)
            addRoute(route = newRoute)
        }

        override fun back() = backImpl()
    }

    val wordListCoordinator = object : WordListCoordinator {
        override fun updateArgs(query: String?) {
            val route = if (section.value!!.second is MainRoute.WordList) {
                MainRoute.WordList(query)
            } else {
                MainRoute.FavoriteList(query)
            }
            backImpl(commit = false)
            addRoute(route = route, commit = false)
        }

        override fun search() {
            val newRoute = if (section.value!!.second is MainRoute.WordList) {
                MainRoute.Search()
            } else {
                MainRoute.FavoriteSearch
            }
            addRoute(route = newRoute)
        }

        override fun details(id: WordDefinitionId) {
            addRoute(route = MainRoute.WordDetails(id))
        }

        override fun back() = backImpl()
    }

    val wordDetailsCoordinator = object : WordDetailsCoordinator {
        override fun back() = backImpl()
    }

    override fun back() = backImpl()
}