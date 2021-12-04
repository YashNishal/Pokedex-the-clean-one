package com.yashnishal.pokedex.util

import androidx.compose.ui.graphics.Color
import com.yashnishal.pokedex.data.remote.response.Stat
import com.yashnishal.pokedex.ui.theme.*
import java.util.*

fun parseTypeToColor(text: String): Color {
    return when(text) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.lowercase(Locale.getDefault())) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.lowercase(Locale.getDefault())) {
        "hp" -> "HP"
        "attack" -> "ATK"
        "defense" -> "DEF"
        "special-attack" -> "SATK"
        "special-defense" -> "SDEF"
        "speed" -> "SPD"
        else -> ""
    }
}