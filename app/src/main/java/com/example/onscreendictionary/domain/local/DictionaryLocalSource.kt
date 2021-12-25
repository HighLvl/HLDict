package com.example.onscreendictionary.domain.local

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import kotlinx.coroutines.flow.Flow

interface DictionaryLocalSource {
    fun getAllFavorite(): Flow<List<WordDefinition>>
    fun getAllByTitle(title: String, favoriteOnly: Boolean): Flow<List<WordDefinition>>
    fun get(id: WordDefinitionId): Flow<WordDefinition?>
    suspend fun getTitlesStartsWith(prefix: String, favoriteOnly: Boolean): List<String>
    suspend fun insert(word: WordDefinition)
    suspend fun insertAll(words: List<WordDefinition>)
    suspend fun delete(id: WordDefinitionId)
}