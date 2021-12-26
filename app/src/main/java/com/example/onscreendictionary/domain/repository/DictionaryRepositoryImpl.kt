package com.example.onscreendictionary.domain.repository

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.domain.local.DictionaryLocalSource
import com.example.onscreendictionary.domain.remote.DictionaryRemoteSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class DictionaryRepositoryImpl @Inject constructor(
    private val local: DictionaryLocalSource,
    private val remote: DictionaryRemoteSource
) : DictionaryRepository {
    override fun getShortWordDefinitions(
        title: String,
        favoriteOnly: Boolean,
        forceFetch: Boolean
    ): Flow<List<WordDefinition>> {
        var forceFetch = forceFetch
        return if (favoriteOnly && title.isEmpty()) {
            local.getAllFavorite()
        } else {
            local.getAllByTitle(title, favoriteOnly).map { words ->
                if (forceFetch || words.isEmpty()) {
                    forceFetch = false
                    remote.getShortWordDefinitions(title).also {
                        local.insertAll(it)
                    }
                } else {
                    words
                }
            }
        }
    }

    override fun getFullWordDefinition(
        id: WordDefinitionId,
        forceFetch: Boolean
    ): Flow<WordDefinition> {
        var forceFetch = forceFetch
        var isLastNotFull = false
        return local.get(id).map { word ->
            if (forceFetch || word == null || (!word.isFull && !isLastNotFull)) {
                isLastNotFull = word != null && !word.isFull
                forceFetch = false
                try {
                    remote.getFullWordDefinition(id).also {
                        local.insert(it)
                    }
                } catch (e: Throwable) {
                    word ?: throw e
                }
            } else {
                word
            }
        }
    }

    override fun getRandomWordDefinitions(favoriteOnly: Boolean): Flow<List<WordDefinition>> {
        return if (!favoriteOnly) {
            flowOf(Unit).flatMapLatest {
                val words = remote.getRandomWordDefinitions()
                local.insertAll(words)
                getShortWordDefinitions(words.first().title, favoriteOnly, false)
            }
        } else {
            throw IllegalArgumentException()
        }
    }

    override suspend fun getWordTitlesStartsWith(
        prefix: String,
        favoriteOnly: Boolean
    ): List<String> {
        return when {
            prefix.isBlank() -> emptyList()
            favoriteOnly -> local.getTitlesStartsWith(prefix, favoriteOnly)
            else -> remote.getWordTitlesStartsWith(prefix)
        }
    }

    override suspend fun setFavorite(id: WordDefinitionId, isFavorite: Boolean) {
        var word = local.get(id).first()
        if (word == null || !word.isFull) {
            try {
                word = remote.getFullWordDefinition(id)
            } catch (e: Throwable) {
                word ?: throw e
            }
        }
        local.insert(word!!.copy(isFavorite = isFavorite))
    }
}