package com.nehak.pokemonlist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nehak.pokemonlist.R

class PokemonListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_list)
    }
}