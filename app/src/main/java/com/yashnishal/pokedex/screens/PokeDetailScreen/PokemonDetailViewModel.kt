package com.yashnishal.pokedex.screens.PokeDetailScreen

import androidx.lifecycle.ViewModel
import com.yashnishal.pokedex.data.remote.response.Pokemon
import com.yashnishal.pokedex.repository.PokemonRepository
import com.yashnishal.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }

}