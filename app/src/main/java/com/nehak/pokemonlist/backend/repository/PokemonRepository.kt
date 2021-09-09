package com.nehak.pokemonlist.backend.repository

import androidx.annotation.WorkerThread
import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.other.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 7/9/21.
 * Repository which fetches data from Remote or Local data sources (RoomDB)
 */
class PokemonRepository @Inject constructor(
    var pokemonRemoteDataSource: PokemonRemoteDataSource,
    var pokemonDao: PokemonDao
) {

    @WorkerThread
    fun fetchPokemonList(
        limit: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var pokemons = pokemonDao.getPokemonList()
        if (pokemons.isEmpty()) {
            val response = pokemonRemoteDataSource.fetchPokemonList(limit)
            if (response.status == ApiResult.Status.SUCCESS) {
                pokemons = response.data!!.results!!;
                pokemonDao.insertPokemonList(pokemons)
                emit(pokemonDao.getPokemonList())
            } else {
                onError("Unable to fetch data! Please retry!")
            }
        } else {
            emit(pokemonDao.getPokemonList())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


}
