package com.nehak.pokemonlist.backend.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class PokemonModel(
    @PrimaryKey
    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String? = null

)