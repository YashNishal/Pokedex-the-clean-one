package com.yashnishal.pokedex.repository


import com.yashnishal.pokedex.data.remote.PokeApi
import com.yashnishal.pokedex.data.remote.response.Pokemon
import com.yashnishal.pokedex.data.remote.response2.PokemonList
import com.yashnishal.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import timber.log.Timber
import javax.inject.Inject
import kotlin.Exception

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api : PokeApi
) {


    suspend fun getPokemonList(limit: Int,offset: Int) : Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e : Exception) {
            return Resource.Error("An error occurred : ${e.toString()}")
        }
        return Resource.Success(response)
    }


    suspend fun getPokemonInfo(pokemonName: String) : Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e : Exception) {
            Timber.e(e.toString())
            return Resource.Error("An error occurred. ${e.toString()}")
        }
        return Resource.Success(response)
    }

}