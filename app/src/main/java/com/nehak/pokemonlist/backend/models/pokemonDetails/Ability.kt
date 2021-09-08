package com.nehak.pokemonlist.backend.models.pokemonDetails

import com.google.gson.annotations.Expose
import com.nehak.pokemonlist.backend.models.pokemonDetails.Ability
import com.google.gson.annotations.SerializedName

class Ability(
    @Expose
    var ability: Ability? = null,

    @SerializedName("is_hidden")
    var isHidden: Boolean? = null,

    @Expose
    var name: String? = null,

    @Expose
    var slot: Long? = null,

    @Expose
    var url: String? = null
)