package com.nehak.pokemonlist.backend.repository

import androidx.annotation.WorkerThread
import com.nehak.pokemonlist.backend.dataSource.PokemonRemoteDataSource
import com.nehak.pokemonlist.backend.database.PokemonDao
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.other.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 7/9/21.
 * Repository which fetches data from Remote or Local data sources (RoomDB)
 */
interface BasePokemonRepository {
    fun fetchPokemonList(
        limit: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<PokemonModel>>;
}
