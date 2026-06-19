package com.example.aotexplorer.network

import com.example.aotexplorer.model.ApiResponse
import com.example.aotexplorer.model.Character
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int = 1
    ): ApiResponse<Character>

    @GET("characters/{id}")
    suspend fun getCharacterById(
        @Path("id") id: Int
    ): Character

    @GET("titans")
    suspend fun getTitans(): ApiResponse<com.example.aotexplorer.model.Titan>
}
