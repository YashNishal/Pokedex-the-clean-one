package com.yashnishal.pokedex.di

import com.yashnishal.pokedex.data.remote.PokeApi
import com.yashnishal.pokedex.repository.PokemonRepository
import com.yashnishal.pokedex.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // no need to for this as dagger hilt can automatically generate this
    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    // needed to be done
    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
}