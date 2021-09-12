package com.nehak.pokemonlist.utils.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.nehak.pokemonlist.backend.models.PokemonModel
import java.util.*

/**
 * Created by Neha Kushwah on 8/9/21.
 */
class DiffCallBackPokemonModel(private val oldList: List<PokemonModel>?, private val newList: List<PokemonModel>?) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList?.size ?: 0
    }

    override fun getNewListSize(): Int {
        return newList?.size ?: 0
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (newItemPosition >= newList!!.size) return false
        return if (oldItemPosition >= oldList!!.size) false else Objects.equals(
            oldList[oldItemPosition],
            newList[newItemPosition]
        )
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (newItemPosition >= newList!!.size) return false
        return if (oldItemPosition >= oldList!!.size) false else Objects.equals(
            oldList[oldItemPosition],
            newList[newItemPosition]
        )
    }
}