package com.nehak.pokemonlist.backend.repository

import androidx.annotation.WorkerThread
import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse
import com.nehak.pokemonlist.backend.other.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

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
        pageNumber: Int,
        limit: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var pokemons = pokemonDao.getPokemonListForPage(pageNumber)
        if (pokemons.isEmpty()) {
            val response: ApiResult<PokemonListResponse?> =
                pokemonRemoteDataSource.fetchPokemonList(pageNumber, limit);
            if (response.status == ApiResult.Status.SUCCESS) {
                pokemons = response.data!!.results!!;
                pokemons.forEach { pokemon ->
                    val id = pokemon.url!!.split("/".toRegex()).dropLast(1).last().toInt()
                    pokemon.id = id;
                    pokemon.pageNumber = pageNumber;
                    // Contains next page URL or not
                    pokemon.hasNextPageUrl = response.data.next != null
                    pokemon.isFromDB = true
                }
                // Insert them in DB, then emit results from DB
                pokemonDao.insertPokemonList(pokemons)
                emit(pokemonDao.getPokemonList())
            } else {
                onError("Unable to fetch data! Please retry!")
            }
        } else {
            emit(pokemonDao.getPokemonList())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

    fun clear(
    ) {
        pokemonDao.deleteAll()
    }

    @WorkerThread
    fun fetchPokemonDetail(
        pokemonName: String,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var pokemonDetail = pokemonDao.getPokemonDetails(pokemonName)
        if (pokemonDetail == null) {
            val response: ApiResult<PokemonDetails?> =
                pokemonRemoteDataSource.fetchPokemonDetails(pokemonName);
            if (response.status == ApiResult.Status.SUCCESS && response.data != null) {
                // Insert it in DB, then emit results from DB
                pokemonDao.insertPokemonDetails(response.data)
                emit(pokemonDao.getPokemonDetails(pokemonName))
            } else {
                onError("Unable to fetch data! Please retry!")
            }
        } else {
            emit(pokemonDetail)
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)


    @WorkerThread
    fun searchPokemonList(
        pokemonName: String?,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {

        if (pokemonName == null || pokemonName.isEmpty()) {
            emit(ArrayList())
            return@flow
        }

        var pokemons = pokemonDao.searchPokemonByName(pokemonName)
        // Emit the pokemon added already in db
        emit(pokemons)

        val response: ApiResult<PokemonModel?> =
            pokemonRemoteDataSource.fetchPokemonByName(pokemonName);
        if (response.status == ApiResult.Status.SUCCESS && response.data != null) {
            if (!pokemons.contains(response.data)) {
                val list = ArrayList<PokemonModel>()
                list.addAll(pokemons)
                emit(list)
                list.add(response.data)
            }
        } else {
            if (pokemons.isEmpty()) {
                emit(pokemons)
                onError("Unable to fetch data! Please retry!")
            }
        }

    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(Dispatchers.IO)

}
