package com.nehak.pokemonlist.backend.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails
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

    suspend fun fetchPokemonList(
        name: String?,
        offset: Int,
        limit: Int
    ): ApiResult<PokemonListResponse> {
        val url: String;
        if (name.isNullOrBlank()) {
            url = "$API_POKEMON?limit=$limit&offset=$offset";
        } else {
            url = "$API_POKEMON?search=$name&limit=$limit&offset=$offset";
        }

        return suspendCancellableCoroutine { continuation ->
            val queue = Volley.newRequestQueue(appContext)
            val myReq: GsonRequest<PokemonListResponse> = GsonRequest(
                Request.Method.GET,
                url,
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

    suspend fun fetchPokemonDetails(name: String): ApiResult<PokemonDetails> {
        return suspendCancellableCoroutine { continuation ->
            val queue = Volley.newRequestQueue(appContext)
            val myReq: GsonRequest<PokemonDetails> = GsonRequest(
                Request.Method.GET,
                "$API_POKEMON/$name",
                PokemonDetails::class.java,
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

    suspend fun fetchPokemonByName(name: String): ApiResult<PokemonModel> {
        return suspendCancellableCoroutine { continuation ->
            val queue = Volley.newRequestQueue(appContext)
            val myReq: GsonRequest<PokemonModel> = GsonRequest(
                Request.Method.GET,
                "$API_POKEMON/$name",
                PokemonModel::class.java,
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