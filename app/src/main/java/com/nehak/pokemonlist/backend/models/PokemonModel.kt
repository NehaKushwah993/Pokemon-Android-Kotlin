package com.nehak.pokemonlist.backend.models

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString()
    ) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pageNumber)
        parcel.writeByte(if (hasNextPageUrl) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PokemonModel> {
        override fun createFromParcel(parcel: Parcel): PokemonModel {
            return PokemonModel(parcel)
        }

        override fun newArray(size: Int): Array<PokemonModel?> {
            return arrayOfNulls(size)
        }
    }
}