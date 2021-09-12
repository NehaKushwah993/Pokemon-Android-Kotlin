package com.nehak.pokemonlist.utils.interfaces

import android.view.View
import com.nehak.pokemonlist.backend.models.PokemonModel

interface OnPokemonClickListener {
    fun onPokemonClick(pos: Int, pokemonModel: PokemonModel, commonView: View)
}