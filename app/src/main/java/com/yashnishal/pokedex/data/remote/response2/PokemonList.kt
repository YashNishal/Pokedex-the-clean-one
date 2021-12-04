package com.yashnishal.pokedex.data.remote.response2

data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)