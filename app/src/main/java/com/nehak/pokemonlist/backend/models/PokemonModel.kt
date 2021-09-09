package com.nehak.pokemonlist.backend.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class PokemonModel(

    var pageNumber: Int = 0,
    var hasNextPageUrl: Boolean = true,

    @PrimaryKey
    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other == null || this::class.java != other::class.java) return false;
        val that: PokemonModel = other as PokemonModel
        return Objects.equals(name, that.name) && Objects.equals(
            pageNumber,
            that.pageNumber
        ) && Objects.equals(url, that.url) && Objects.equals(hasNextPageUrl, that.hasNextPageUrl)
    }

    override fun hashCode(): Int {
        return Objects.hash(name, url, pageNumber, hasNextPageUrl)
    }
}