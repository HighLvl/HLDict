package com.example.onscreendictionary.ui.search

import com.example.onscreendictionary.ui.base.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchArgs(
    val query: String?,
    val favoriteOnly: Boolean
) : Args