package com.nehak.pokemonlist.backend.models.pokemonDetails

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity
data class PokemonDetails(

    @SerializedName("base_experience")
    var baseExperience: Long = 0,

    @Expose
    var height: Long = 0,

    @Expose
    var id: Long = 0,

    @PrimaryKey
    @Expose
    var name: String,

    @Expose
    var weight: Long = 0
) {
    fun heightAsDouble(): Double {
        return height.toDouble()
    }

    fun weightAsDouble(): Double {
        return weight.toDouble()
    }
}