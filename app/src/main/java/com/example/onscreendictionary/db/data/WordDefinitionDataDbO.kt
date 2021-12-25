package com.example.onscreendictionary.db.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordDefinitionDataDbO(
    @SerialName("syllables")
    val syllables: String,
    @SerialName("pos")
    val pos: String,
    @SerialName("gloss")
    val gloss: String,
    @SerialName("examples")
    val examples: List<String>,
    @SerialName("synonyms")
    val synonyms: String,
    @SerialName("antonyms")
    val antonyms: String,

    @SerialName("hyponyms")
    val hyponyms: String? = null,
    @SerialName("hypernyms")
    val hypernyms: String? = null,
    @SerialName("etymology")
    val etymology: String? = null,
    @SerialName("phras")
    val phras: List<String>? = null,
    @SerialName("ipa")
    val ipa: String? = null
)