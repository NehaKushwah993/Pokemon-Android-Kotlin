package com.nehak.pokemonlist.backend.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity
data class PokemonModel(
    @SerializedName("pageNumber")
    var pageNumber: Int = 0,
    var hasNextPageUrl: Boolean = true,
    var isFromDB: Boolean = false,

    @PrimaryKey
    @SerializedName("name")
    var name: String,

    @SerializedName("url")
    var url: String? = null,

    @SerializedName("id")
    var id: Int = 0,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || this::class.java != other::class.java) return false;
        val that: PokemonModel = other as PokemonModel
        return Objects.equals(name, that.name)
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pageNumber)
        parcel.writeByte(if (hasNextPageUrl) 1 else 0)
        parcel.writeByte(if (isFromDB) 1 else 0)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeInt(id)
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

    fun capitaliseName(): String {
        return name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}