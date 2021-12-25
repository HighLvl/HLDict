package com.example.onscreendictionary.db.source

import com.example.onscreendictionary.db.mapper.WordDefinitionDbMapper
import com.example.onscreendictionary.db.mapper.WordDefinitionIdDbMapper
import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.domain.local.DictionaryLocalSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

class DictionaryLocalSourceImpl @Inject constructor(
    private val dao: DictionaryDao
) : DictionaryLocalSource {
    override fun getAllFavorite(): Flow<List<WordDefinition>> {
        return dao.getAllFavorite().map {
            withContext(Dispatchers.IO) {
                it.map(WordDefinitionDbMapper::mapToDomain)
            }
        }
    }

    override fun getAllByTitle(title: String, favoriteOnly: Boolean): Flow<List<WordDefinition>> {
        return dao.getAllByTitle(title, favoriteOnly).map {
            withContext(Dispatchers.IO) {
                it.map(WordDefinitionDbMapper::mapToDomain)
            }
        }
    }

    override fun get(id: WordDefinitionId): Flow<WordDefinition?> {
        val idDbO = WordDefinitionIdDbMapper.mapFromDomain(id)
        return dao.getById(id.title, idDbO.lang, idDbO.sense, idDbO.gloss).map {
            withContext(Dispatchers.IO) {
                it?.let(WordDefinitionDbMapper::mapToDomain)
            }
        }
    }

    override suspend fun getTitlesStartsWith(prefix: String, favoriteOnly: Boolean): List<String> {
        return dao.getTitlesMatches("$prefix*", favoriteOnly)
    }

    override suspend fun insert(word: WordDefinition) {
        val timestamp = Instant.now().toEpochMilli()
        dao.insert(WordDefinitionDbMapper.mapFromDomain(word, timestamp))
    }

    override suspend fun insertAll(words: List<WordDefinition>) {
        val timestamp = Instant.now().toEpochMilli()
        dao.insertAll(words.map { WordDefinitionDbMapper.mapFromDomain(it, timestamp) })
    }

    override suspend fun delete(id: WordDefinitionId) {
        val idDbO = WordDefinitionIdDbMapper.mapFromDomain(id)
        dao.delete(id.title, idDbO.lang, idDbO.sense, idDbO.gloss)
    }
}