package com.example.onscreendictionary.domain.data

data class WordDefinition(
    val id: WordDefinitionId,
    val isFull: Boolean,
    val isFavorite: Boolean,
    val title: String,
    val syllables: String,
    val pos: String,
    val gloss: String,
    val lang: String,
    val examples: List<String>,
    val synonyms: String,
    val antonyms: String,

    val hyponyms: String?,
    val hypernyms: String?,
    val etymology: String?,
    val phras: List<String>?,
    val ipa: String?
)