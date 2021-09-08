package com.nehak.pokemonlist.backend.models.pokemonDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    @Expose
    var abilities: List<Ability>? = null,

    @SerializedName("base_experience")
    var baseExperience: Long = 0,

    @Expose
    var height: Long = 0,

    @Expose
    var id: Long = 0,

    @Expose
    var name: String? = null,

    @Expose
    var weight: Long = 0
)