package com.nehak.pokemonlist.backend.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class PokemonModel(
    @PrimaryKey
    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || this::class.java != other::class.java) return false;
        val that: PokemonModel = other as PokemonModel
        return Objects.equals(name, that.name) && Objects.equals(url, that.url)
    }

    override fun hashCode(): Int {
        return Objects.hash(name, url)
    }
}