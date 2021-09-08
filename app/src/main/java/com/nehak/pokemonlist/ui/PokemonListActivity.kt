package com.nehak.pokemonlist.ui.pokemonList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.ui.PokemonListViewModel

/**
 * Created by Neha Kushwah on 7/9/21.
 */
class PokemonListActivity : AppCompatActivity() {

    private val pokemonListViewModel: PokemonListViewModel by lazy {
        ViewModelProvider(this).get(PokemonListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)
    }
}