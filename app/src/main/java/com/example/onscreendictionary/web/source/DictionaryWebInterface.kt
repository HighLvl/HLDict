package com.example.onscreendictionary.web.source

import com.example.onscreendictionary.web.data.WordDefinitionWebO
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryWebInterface {
    @GET("/findTitlesHavingPrefix/{prefix}")
    suspend fun getWordTitlesStartsWith(
        @Path("prefix")
        prefix: String
    ): List<String>

    @GET("/getShortGlossList/{title}")
    suspend fun getShortWordDefinitions(
        @Path("title")
        title: String
    ): List<WordDefinitionWebO>

    @GET("/getFullGloss/{title}/{langNum}/{senseNum}/{glossNum}")
    suspend fun getFullWordDefinition(
        @Path("title")
        title: String,
        @Path("langNum")
        langNum: Int,
        @Path("senseNum")
        senseNum: Int,
        @Path("glossNum")
        glossNum: Int
    ): WordDefinitionWebO

    @GET("/getShortGlossListByRandomTitle")
    suspend fun getRandomWordDefinitions(
    ): List<WordDefinitionWebO>
}