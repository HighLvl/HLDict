package com.example.onscreendictionary.ui.main

import androidx.fragment.app.FragmentManager
import com.example.onscreendictionary.ui.reminder.ReminderFragment
import com.example.onscreendictionary.ui.search.SearchArgs
import com.example.onscreendictionary.ui.search.SearchFragment
import com.example.onscreendictionary.ui.word_details.WordDetailsFragment
import com.example.onscreendictionary.ui.word_list.WordListArgs
import com.example.onscreendictionary.ui.word_list.WordListFragment

class MainNavigator(
    private val fm: FragmentManager,
    private val containerId: Int
) {
    fun setRoute(route: MainRoute) {
        val fragment = when (route) {
            is MainRoute.Search -> SearchFragment(SearchArgs(route.query, false))
            is MainRoute.WordList -> WordListFragment(WordListArgs(route.query, false))
            is MainRoute.WordDetails -> WordDetailsFragment(route.wordId)
            is MainRoute.FavoriteList -> WordListFragment(WordListArgs(route.query, true))
            MainRoute.FavoriteSearch -> SearchFragment(SearchArgs("", true))
            MainRoute.Reminder -> ReminderFragment()
        }
        fm.beginTransaction()
            .replace(containerId, fragment)
            .commitNowAllowingStateLoss()
    }
}