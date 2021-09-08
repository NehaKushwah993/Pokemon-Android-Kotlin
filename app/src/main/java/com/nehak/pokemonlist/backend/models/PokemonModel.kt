package com.nehak.pokemonlist.backend.models

import com.google.gson.annotations.SerializedName

class PokemonModel(
    @SerializedName("name")
    var name: String? = null,

    @SerializedName("url")
    var url: String? = null
)