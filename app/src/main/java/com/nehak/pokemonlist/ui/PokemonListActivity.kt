package com.nehak.pokemonlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nehak.pokemonlist.databinding.ActivityPokemonListBinding
import com.nehak.pokemonlist.utils.LocalLogs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonListActivity : AppCompatActivity() {

    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonListBinding
    lateinit var adapter: PokemonAdapter
    @VisibleForTesting
    lateinit var pokemonListViewModel: PokemonListViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonListBinding.inflate(LayoutInflater.from(this));
        viewBinding.lifecycleOwner = this;
        setContentView(viewBinding.root)

        initViewModel()
        initAdapter()
        addObservers();

    }

    private fun initAdapter() {
        adapter = PokemonAdapter();
        viewBinding.rvPokemonList.adapter = adapter
    }

    private fun initViewModel() {
        pokemonListViewModel =
            ViewModelProvider(this).get(PokemonListViewModel::class.java);
    }

    private fun addObservers() {

        lifecycleScope.launch {
            pokemonListViewModel.pokemonList.collect { pokemonList ->
                adapter.setPokemonList(pokemonList)
            }
        }

        lifecycleScope.launch {
            pokemonListViewModel.errorMessage.collect { errorMessage ->
                LocalLogs.debug("Received errorMessage $errorMessage")
            }
        }

        lifecycleScope.launch {
            pokemonListViewModel.isLoading.collect { isLoading ->
                LocalLogs.debug("Received isLoading $isLoading")
                viewBinding.isLoading = isLoading
            }
        }
    }
}