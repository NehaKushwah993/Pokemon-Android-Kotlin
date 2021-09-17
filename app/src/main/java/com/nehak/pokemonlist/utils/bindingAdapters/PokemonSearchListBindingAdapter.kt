package com.nehak.pokemonlist.utils.bindingAdapters

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import com.nehak.pokemonlist.ui.pokemonSearch.PokemonSearchViewModel

object PokemonSearchListBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["setSearchListener"],
    )
    fun setSearchListener(
        searchView: SearchView,
        viewModel: PokemonSearchViewModel?,
    ) {
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus();
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel?.setSearchQuery(it) }
                return true
            }
        }
        )
    }

}