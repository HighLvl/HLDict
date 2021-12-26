package com.example.onscreendictionary.domain.repository

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun getShortWordDefinitions(
        title: String,
        favoriteOnly: Boolean,
        forceFetch: Boolean
    ): Flow<List<WordDefinition>>

    fun getFullWordDefinition(id: WordDefinitionId, forceFetch: Boolean): Flow<WordDefinition>
    fun getRandomWordDefinitions(favoriteOnly: Boolean): Flow<List<WordDefinition>>
    suspend fun getWordTitlesStartsWith(prefix: String, favoriteOnly: Boolean): List<String>
    suspend fun setFavorite(id: WordDefinitionId, isFavorite: Boolean)
}