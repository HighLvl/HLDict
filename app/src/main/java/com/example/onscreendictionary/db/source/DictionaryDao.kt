package com.example.onscreendictionary.db.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.onscreendictionary.db.data.WordDefinitionDbO
import kotlinx.coroutines.flow.Flow

@Dao
interface DictionaryDao {
    @Query("SELECT * FROM dictionary WHERE favorite ORDER BY updated_at DESC")
    fun getAllFavorite(): Flow<List<WordDefinitionDbO>>

    @Query("SELECT * FROM dictionary WHERE id_title = :title AND (NOT :favoriteOnly OR favorite = :favoriteOnly) ORDER BY id_lang, id_sense, id_gloss")
    fun getAllByTitle(title: String, favoriteOnly: Boolean): Flow<List<WordDefinitionDbO>>

    @Query("SELECT * FROM dictionary WHERE id_title = :title AND id_lang = :lang AND id_sense = :sense AND id_gloss = :gloss LIMIT 1")
    fun getById(title: String, lang: Int, sense: Int, gloss: Int): Flow<WordDefinitionDbO?>

    @Query("SELECT DISTINCT id_title FROM dictionary WHERE id_title GLOB :pattern AND (NOT :favoriteOnly OR favorite = :favoriteOnly) ORDER BY updated_at DESC LIMIT 10")
    suspend fun getTitlesMatches(pattern: String, favoriteOnly: Boolean): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: WordDefinitionDbO)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(words: List<WordDefinitionDbO>)

    @Query("DELETE FROM dictionary WHERE id_title = :title AND id_lang = :lang AND id_sense = :sense AND id_gloss = :gloss")
    suspend fun delete(title: String, lang: Int, sense: Int, gloss: Int)
}