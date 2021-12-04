package com.yashnishal.pokedex.data.remote

import com.yashnishal.pokedex.data.remote.response.Pokemon
import com.yashnishal.pokedex.data.remote.response2.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList


    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon

}