package com.nehak.pokemonlist.repository

import androidx.lifecycle.liveData
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.repository.BasePokemonRepository
import com.nehak.pokemonlist.util.MockUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Created by Neha Kushwah on 8/9/21.
 * Repository which provides fake data for testing.
 */
class FakePokemonRepository : BasePokemonRepository {
    override fun fetchPokemonList(
        limit: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<PokemonModel>> {
        val list = MockUtil.mockPokemonList(limit)

        var data = flow {
            emit(list)
        }
        return data
    }

}