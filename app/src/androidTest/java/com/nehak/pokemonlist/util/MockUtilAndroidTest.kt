package com.nehak.pokemonlist.util

import com.nehak.pokemonlist.backend.models.PokemonModel

/**
 * Created by Neha Kushwah on 7/9/21.
 */
object MockUtilAndroidTest {

    fun mockPokemon() = PokemonModel(
        name = "Pokemon Name",
        url = "https://pokeapi.co/api/v2/pokemon/45/"
    )

    fun mockPokemonList(size: Int): ArrayList<PokemonModel> {
        var mockModels = ArrayList<PokemonModel>()
        for (i in 1..size) {
            mockModels.add(mockPokemon());
        }
        return mockModels;
    }

}