package com.nehak.pokemonlist.util

import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails

/**
 * Created by Neha Kushwah on 7/9/21.
 */
object MockUtil {

    fun mockPokemon() = PokemonModel(
        name = "pikachu",
        url = "https://pokeapi.co/api/v2/pokemon/45/"
    )

    fun mockPokemonList(size: Int): ArrayList<PokemonModel> {
        var mockModels = ArrayList<PokemonModel>()
        for (i in 1..size) {
            mockModels.add(mockPokemon());
        }
        return mockModels;
    }

    fun mockPokemonDetail() = PokemonDetails(
        id = 25,
        name = "pikachu",
        height = 4,
        weight = 60,
        baseExperience = 112
    )

}