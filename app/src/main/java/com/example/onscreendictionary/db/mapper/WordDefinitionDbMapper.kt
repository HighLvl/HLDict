package com.example.onscreendictionary.db.mapper

import com.example.onscreendictionary.db.data.WordDefinitionDataDbO
import com.example.onscreendictionary.db.data.WordDefinitionDbO
import com.example.onscreendictionary.domain.data.WordDefinition
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object WordDefinitionDbMapper {
    fun mapToDomain(data: WordDefinitionDbO): WordDefinition = with(data) {
        val jsonData = Json.decodeFromString<WordDefinitionDataDbO>(data.data)
        return WordDefinition(
            id = WordDefinitionIdDbMapper.mapToDomain(id),
            isFull = isFull,
            isFavorite = isFavorite,
            title = id.title,
            lang = lang,
            syllables = jsonData.syllables,
            pos = jsonData.pos,
            gloss = jsonData.gloss,
            examples = jsonData.examples,
            synonyms = jsonData.synonyms,
            antonyms = jsonData.antonyms,

            hyponyms = jsonData.hyponyms,
            hypernyms = jsonData.hypernyms,
            etymology = jsonData.etymology,
            phras = jsonData.phras,
            ipa = jsonData.ipa
        )
    }

    fun mapFromDomain(data: WordDefinition, updatedAt: Long): WordDefinitionDbO = with(data) {
        val jsonData = Json.encodeToString(
            WordDefinitionDataDbO(
                syllables = syllables,
                pos = pos,
                gloss = gloss,
                examples = examples,
                synonyms = synonyms,
                antonyms = antonyms,

                hyponyms = hyponyms,
                hypernyms = hypernyms,
                etymology = etymology,
                phras = phras,
                ipa = ipa
            )
        )
        WordDefinitionDbO(
            id = WordDefinitionIdDbMapper.mapFromDomain(id),
            updatedAt = updatedAt,
            isFull = isFull,
            isFavorite = isFavorite,
            lang = lang,
            data = jsonData
        )
    }
}