package com.nehak.pokemonlist.backend.models.pokemonList

import com.google.gson.annotations.SerializedName
import com.nehak.pokemonlist.backend.models.PokemonModel

data class PokemonListModel(
    @SerializedName("count")
    var count: Long? = null,

    @SerializedName("next")
    var next: String? = null,

    @SerializedName("previous")
    var previous: Any? = null,

    @SerializedName("results")
    var results: List<PokemonModel>? = null
)