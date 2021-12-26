package com.example.onscreendictionary.domain.remote

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId

interface DictionaryRemoteSource {
    suspend fun getShortWordDefinitions(title: String): List<WordDefinition>
    suspend fun getFullWordDefinition(id: WordDefinitionId): WordDefinition
    suspend fun getRandomWordDefinitions(): List<WordDefinition>
    suspend fun getWordTitlesStartsWith(prefix: String): List<String>
}