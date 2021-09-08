package com.nehak.pokemonlist.ui

import androidx.lifecycle.ViewModel
import com.nehak.pokemonlist.backend.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 8/9/21.
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {
}