package com.nehak.pokemonlist.utils

import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse

/**
 * Created by Neha Kushwah on 7/9/21.
 */
object PokemonMockUtil {

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

    fun mockPokemonResponse(size: Int): PokemonListResponse {
        var pokemonListResponse = PokemonListResponse()
        pokemonListResponse.count = size.toLong()
        pokemonListResponse.previous = null
        pokemonListResponse.next = null
        pokemonListResponse.results = mockPokemonList(size)
        return pokemonListResponse;
    }

}