package com.example.onscreendictionary.db.data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class WordDefinitionDbO(
    @PrimaryKey
    @Embedded(prefix = "id_")
    val id: WordDefinitionIdDbO,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "full")
    val isFull: Boolean,
    @ColumnInfo(name = "favorite")
    val isFavorite: Boolean,
    @ColumnInfo(name = "lang")
    val lang: String,
    @ColumnInfo(name = "data")
    val data: String
)

