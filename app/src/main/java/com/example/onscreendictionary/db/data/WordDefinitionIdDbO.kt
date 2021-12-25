package com.example.onscreendictionary.db.data

import androidx.room.ColumnInfo

data class WordDefinitionIdDbO(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "lang")
    val lang: Int,
    @ColumnInfo(name = "sense")
    val sense: Int,
    @ColumnInfo(name = "gloss")
    val gloss: Int
)