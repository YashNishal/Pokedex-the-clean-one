package com.example.pokedex.screens.PokeDetailScreen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.pokedex.R
import com.example.pokedex.data.remote.response.Pokemon
import com.example.pokedex.ui.theme.*
import com.example.pokedex.util.*
import com.example.pokedex.util.Constants.COLOR_LIST
import java.util.*


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 40.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .background(dominantColor)
            .background(if (isSystemInDarkTheme()) MaterialTheme.colors.background else offBackground)
    ) {

        //detail screen
        DetailsWrapper(
            pokemonInfo = pokemonInfo,

            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                )
                .shadow(10.dp, RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .clip(RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp))
                .background(if (isSystemInDarkTheme()) MaterialTheme.colors.background else offBackground)
                .padding(32.dp)
                .align(Alignment.BottomCenter),

            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            dominantColor
        )
        // Back Button
        Icon(
            painter = painterResource(id = R.drawable.ic_icons8_back),
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(start = 22.dp, top = 32.dp)
                .align(Alignment.TopStart)
                .size(36.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}


@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun DetailsWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    dominantColor: Color
) {
    when (pokemonInfo) {
        is Resource.Success -> {
            PokemonDetailSection(pokemonInfo.data!!, dominantColor)
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon,
    dominantColor: Color,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(start = 32.dp,end = 32.dp, top = 22.dp)
    ) {
        Text(
            text = pokemonInfo.name,
            color = if (!isSystemInDarkTheme()) headingColor else Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterHorizontally)
        )
        Text(
            text = pokemonInfo.id.toString(),
            color = if (!isSystemInDarkTheme()) subheadingColor else Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(CenterHorizontally)
        )

        ImageCard(pokemonInfo, dominantColor)

        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(pokemonInfo.types.size) { idx ->
                val text = pokemonInfo.types[idx].type.name
                val color = parseTypeToColor(text)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                        .shadow(3.dp, RoundedCornerShape(40.dp))
                        .clip(RoundedCornerShape(40.dp))
                        .background(color)
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                ) {
                    Text(
                        text = text.capitalize(Locale.ROOT),
//                        color = MaterialTheme.colors.onSurface,
                        color = MaterialTheme.colors.onPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        var infoSection by remember { mutableStateOf(false) }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {


            Text(
                text = "Stats",
                color = if(!infoSection) MaterialTheme.colors.onSurface else MaterialTheme.colors.secondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        if(infoSection)
                        infoSection = false
                    }
            )

            Text(
                text = "Info",
                color = if(infoSection) MaterialTheme.colors.onSurface else MaterialTheme.colors.secondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        if(!infoSection)
                            infoSection = true
                    }

            )
        }


        // info section
        AnimatedVisibility(visible = infoSection) {
            InfoSection(pokemonInfo)
        }


        AnimatedVisibility(visible = !infoSection) {
            Stats(pokemonInfo)
        }

    }
}

@Composable
fun Stats(
    pokemonInfo: Pokemon,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf { it.baseStat }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        for (i in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat,
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem
            )
        }
    }
}


@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if(animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
                color = statsBoxHeadingColor
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold,
                color = statsBoxPercentageColor
            )
        }
    }
}

@Composable
fun InfoSection(pokemonInfo: Pokemon) {

    var abilityList = ""
    pokemonInfo.abilities.forEach { ability ->
        abilityList += " " + ability.ability.name + ","
    }
    abilityList = abilityList.dropLast(1)

    val infoListHeading = listOf(
        "Weight",
        "Height",
        "Abilities"
    )
    val infoList = listOf(
        "${pokemonInfo.weight / 10.0} Kg",
        "${pokemonInfo.height / 10.0} m",
        abilityList
    )

    Row(modifier = Modifier.padding(vertical = 16.dp)) {
        Column(modifier = Modifier.padding(end = 16.dp)) {
            for (item in infoListHeading) {
                Text(
                    text = item,
                    color = if (isSystemInDarkTheme()) offBackground else subheadingColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
        Column(modifier = Modifier.padding(end = 16.dp)) {
            for (item in infoList) {
                Text(
                    text = item,
                    color = MaterialTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }
        }
    }


}

@ExperimentalCoilApi
@Composable
fun ImageCard(pokemonInfo: Pokemon, dominantColor: Color, imageSize: Dp = 250.dp) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .shadow(5.dp, RoundedCornerShape(25.dp))
            .clip(RoundedCornerShape(25.dp))
            .background(dominantColor)

    ) {
        Image(
            painter = rememberImagePainter(
                data = pokemonInfo.sprites.other.officialArtwork.frontDefault,
                builder = {
                    crossfade(true)
                    build()
                }
            ),
            contentDescription = pokemonInfo.name,
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 48.dp)
                .size(imageSize)
        )
    }
}

