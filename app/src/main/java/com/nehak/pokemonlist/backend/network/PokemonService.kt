package com.nehak.pokemonlist.backend.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.nehak.pokemonlist.backend.models.pokemonList.PokemonListResponse
import com.nehak.pokemonlist.backend.other.ApiResult
import com.nehak.pokemonlist.backend.other.GsonRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 7/9/21.
 * Pokemon Remote API Service
 */
class PokemonService @Inject constructor(@ApplicationContext var appContext: Context) {

    suspend fun fetchPokemonList(limit: Int): ApiResult<PokemonListResponse> {
        return suspendCancellableCoroutine { continuation ->
            val queue = Volley.newRequestQueue(appContext)
            val myReq: GsonRequest<PokemonListResponse> = GsonRequest(
                Request.Method.GET,
                "$API_POKEMON_LIST?limit=$limit",
                PokemonListResponse::class.java,
                null,
                { response ->
                    continuation.resumeWith(Result.success(ApiResult.success(response)));
                },
                {
                    continuation.resumeWith(Result.success(ApiResult.error("Error")));
                })
            queue.add(myReq)
        }
    }


}