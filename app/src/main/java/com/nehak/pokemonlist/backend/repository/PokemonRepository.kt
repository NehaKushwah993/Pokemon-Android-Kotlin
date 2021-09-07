package com.nehak.pokemonlist.backend.repository

import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDatabase
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 7/9/21.
 * Repository which fetches data from Remote or Local data sources (RoomDB)
 */
class PokemonRepository @Inject constructor(
    private val pokemonDataSource: PokemonRemoteDataSource,
    private val pokemonDatabase: PokemonDatabase
) {

}