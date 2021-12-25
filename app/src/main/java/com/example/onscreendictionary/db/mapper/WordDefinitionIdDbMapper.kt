package com.example.onscreendictionary.db.mapper

import com.example.onscreendictionary.db.data.WordDefinitionIdDbO
import com.example.onscreendictionary.domain.data.WordDefinitionId

object WordDefinitionIdDbMapper {
    fun mapToDomain(data: WordDefinitionIdDbO): WordDefinitionId = with(data) {
        WordDefinitionId(
            title = title,
            lang = lang.takeIf { it != -1 },
            sense = sense.takeIf { it != -1 },
            gloss = gloss.takeIf { it != -1 }
        )
    }

    fun mapFromDomain(data: WordDefinitionId): WordDefinitionIdDbO = with(data) {
        WordDefinitionIdDbO(
            title = title,
            lang = lang ?: -1,
            sense = sense ?: -1,
            gloss = gloss ?: -1
        )
    }
}