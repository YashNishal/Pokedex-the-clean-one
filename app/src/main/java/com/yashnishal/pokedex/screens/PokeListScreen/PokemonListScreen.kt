package com.yashnishal.pokedex.screens.PokeListScreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.yashnishal.pokedex.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.yashnishal.pokedex.data.models.PokedexListEntry
import com.yashnishal.pokedex.ui.theme.*
import com.yashnishal.pokedex.util.Constants.COLOR_LIST

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Pokédex",
                color = if (!isSystemInDarkTheme()) headingColor else Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            )
            Text(
                "Search for a Pokémon by name or using its National Pokédex number. ",
                color = if (!isSystemInDarkTheme()) subheadingColor else Color.White,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            )
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                hint = "Name or number"
            ) {
                viewModel.searchPokemonList(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
            PokeGrid(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }




    Box(modifier = modifier) {
        TextField(
            value = text,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(text = hint, color = hintColor)
            },
            onValueChange = {
                text = it
                onSearch(it)
            },
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = offBackground,
                textColor = headingColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = headingColor
            ),
            shape = RoundedCornerShape(10.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_icons8_search_2),
                    contentDescription = "search",
                    tint = iconTint,
                    modifier = Modifier.size(28.dp)
                )
            }
        )
    }


}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PokeGrid(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(start = 26.dp, end = 26.dp)
    ) {
        items(pokemonList.size) { index ->
            if (index >= pokemonList.size - 1 && !endReached && !isLoading && !isSearching) {
                viewModel.loadPokemonPaginated()
            }
            PokeItem(entry = pokemonList[index], navController = navController)
        }
    }

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }


}

@ExperimentalCoilApi
@Composable
fun PokeItem(
    entry: PokedexListEntry,
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {


    var dominantColor by remember { mutableStateOf(COLOR_LIST.random()) }

    val context = LocalContext.current

    val painter = rememberImagePainter(
        entry.imageUrl,
        builder = {
            crossfade(true)
            build()
            // for using dominant color
            viewModel.fetchColors(entry.imageUrl, context = context) {
                dominantColor = it
            }
        }
    )

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(dominantColor)
            .clickable {
                navController.navigate(
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }

    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .size(128.dp)

        )
        Text(
            text = entry.pokemonName,
            fontWeight = FontWeight.Bold,
            color = headingColor,
            fontSize = 18.sp
        )
        Text(
            text = entry.number.toString(),
            color = subheadingColor,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
        )


    }
}


@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = error,
            color = if (!isSystemInDarkTheme()) subheadingColor else Color.White,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    PokemonListScreen(navController = navController)
}