package com.nehak.pokemonlist.util

import com.nehak.pokemonlist.backend.models.PokemonModel

object MockUtil {

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