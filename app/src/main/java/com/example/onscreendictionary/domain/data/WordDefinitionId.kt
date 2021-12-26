package com.example.onscreendictionary.domain.data

import com.example.onscreendictionary.ui.base.Args
import kotlinx.parcelize.Parcelize

@Parcelize
data class WordDefinitionId(
    val title: String,
    val lang: Int?,
    val sense: Int?,
    val gloss: Int?
) : Args