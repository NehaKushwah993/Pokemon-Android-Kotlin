package com.nehak.pokemonlist.backend.models.pokemonDetails

import com.google.gson.annotations.SerializedName

data class Animated (
    @SerializedName("back_default")
    var backDefault: String? = null,

    @SerializedName("back_female")
    var backFemale: Any? = null,

    @SerializedName("back_shiny")
    var backShiny: String? = null,

    @SerializedName("back_shiny_female")
    var backShinyFemale: Any? = null,

    @SerializedName("front_default")
    var frontDefault: String? = null,

    @SerializedName("front_female")
    var frontFemale: Any? = null,

    @SerializedName("front_shiny")
    var frontShiny: String? = null,

    @SerializedName("front_shiny_female")
    var frontShinyFemale: Any? = null
)