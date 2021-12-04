package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.pokedex.screens.PokeDetailScreen.PokemonDetailScreen
import com.example.pokedex.screens.PokeListScreen.PokemonListScreen
import com.example.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@ExperimentalCoilApi
@ExperimentalFoundationApi
@AndroidEntryPoint
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            PokeDexTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "pokemon_list_screen"
                    ) {
                        composable("pokemon_list_screen") {
                            PokemonListScreen(navController = navController)
                        }
                        composable(
                            "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                            arguments = listOf(
                                navArgument("dominantColor") {
                                    type = NavType.IntType
                                },
                                navArgument("pokemonName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val dominantColor = remember {
                                val color = it.arguments?.getInt("dominantColor")
//                                color?.let { Color(it) } ?: Color.White
                                if (color != null)
                                    Color(color)
                                else
                                    Color.White
                            }

                            val pokemonName = remember {
                                it.arguments?.getString("pokemonName")
                            }

                            PokemonDetailScreen(
                                dominantColor = dominantColor,
                                pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                                navController = navController,
                            )

                        }
                    }
                }
            }
        }
    }
}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    PokemonListScreen(navController = navController)
}