package com.example.onscreendictionary.web.mapper

import com.example.onscreendictionary.domain.data.WordDefinition
import com.example.onscreendictionary.domain.data.WordDefinitionId
import com.example.onscreendictionary.web.data.WordDefinitionWebO

object WordDefinitionWebMapper {
    fun mapToDomain(data: WordDefinitionWebO, isFull: Boolean): WordDefinition = with(data) {
        WordDefinition(
            id = WordDefinitionId(
                title = title,
                lang = langNum,
                sense = senseNum,
                gloss = glossNum
            ),
            isFull = isFull,
            isFavorite = false,
            title = title,
            syllables = syllables.ifBlank { title },
            pos = pos,
            gloss = gloss,
            lang = lang,
            examples = examples,
            synonyms = synonyms,
            antonyms = antonyms,

            hyponyms = hyponyms,
            hypernyms = hypernyms,
            etymology = etymology,
            phras = phras,
            ipa = ipa
        )
    }
}