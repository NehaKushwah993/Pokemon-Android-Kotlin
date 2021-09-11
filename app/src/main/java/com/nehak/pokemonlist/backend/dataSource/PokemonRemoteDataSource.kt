package com.nehak.pokemonlist.backend.dataSource

import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse
import com.nehak.pokemonlist.backend.network.PokemonService
import com.nehak.pokemonlist.backend.other.ApiResult
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 7/9/21.
 * fetches data from remote source
 */
class PokemonRemoteDataSource @Inject constructor(var pokemonService: PokemonService) {
    suspend fun fetchPokemonList(pageNumber: Int, limit: Int): ApiResult<PokemonListResponse?> {
        return pokemonService.fetchPokemonList(pageNumber * limit, limit);
    }

    suspend fun fetchPokemonDetails(pokemonName:String): ApiResult<PokemonDetails?> {
        return pokemonService.fetchPokemonDetails(pokemonName);
    }

    suspend fun fetchPokemonByName(name:String): ApiResult<PokemonModel?> {
        return pokemonService.fetchPokemonByName(name);
    }

}