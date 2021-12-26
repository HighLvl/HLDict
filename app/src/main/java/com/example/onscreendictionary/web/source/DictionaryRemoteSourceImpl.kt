package com.example.onscreendictionary.web.source

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.domain.remote.DictionaryRemoteSource
import com.example.onscreendictionary.web.mapper.WordDefinitionWebMapper
import javax.inject.Inject

class DictionaryRemoteSourceImpl @Inject constructor(
    private val webInterface: DictionaryWebInterface
) : DictionaryRemoteSource {
    override suspend fun getShortWordDefinitions(title: String): List<WordDefinition> {
        return webInterface.getShortWordDefinitions(title).map {
            WordDefinitionWebMapper.mapToDomain(it, isFull = false)
        }
    }

    override suspend fun getFullWordDefinition(id: WordDefinitionId): WordDefinition {
        return WordDefinitionWebMapper.mapToDomain(
            webInterface.getFullWordDefinition(
                id.title,
                id.lang ?: 0,
                id.sense ?: 0,
                id.gloss ?: 0
            ),
            isFull = true
        )
    }

    override suspend fun getRandomWordDefinitions(): List<WordDefinition> {
        return webInterface.getRandomWordDefinitions().map {
            WordDefinitionWebMapper.mapToDomain(it, isFull = false)
        }
    }

    override suspend fun getWordTitlesStartsWith(prefix: String): List<String> {
        return webInterface.getWordTitlesStartsWith(prefix)
    }
}