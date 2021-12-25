package com.example.onscreendictionary.ui.word_list

import com.example.onscreendictionary.domain.data.WordDefinitionId

interface WordListCoordinator {
    fun updateArgs(query: String?)

    fun search()
    fun details(id: WordDefinitionId)
    fun back()
}