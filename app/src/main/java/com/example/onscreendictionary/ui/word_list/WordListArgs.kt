package com.example.onscreendictionary.ui.word_list

import com.example.onscreendictionary.ui.base.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordListArgs(
    val query: String?,
    val favoriteOnly: Boolean
) : Args