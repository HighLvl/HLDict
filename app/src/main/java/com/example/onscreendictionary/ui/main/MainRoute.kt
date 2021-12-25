package com.example.onscreendictionary.ui.main

import android.os.Parcelable
import com.example.onscreendictionary.domain.data.WordDefinitionId
import kotlinx.parcelize.Parcelize


sealed class MainRoute : Parcelable {
    @Parcelize
    data class WordList(
        val query: String? = null
    ) : MainRoute()

    @Parcelize
    data class Search(
        val query: String? = null
    ) : MainRoute()

    @Parcelize
    data class WordDetails(
        val wordId: WordDefinitionId
    ) : MainRoute()

    @Parcelize
    object Reminder : MainRoute()

    @Parcelize
    data class FavoriteList(
        val query: String? = null
    ) : MainRoute()

    @Parcelize
    object FavoriteSearch : MainRoute()
}