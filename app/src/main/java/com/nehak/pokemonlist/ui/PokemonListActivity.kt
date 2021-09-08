package com.nehak.pokemonlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nehak.pokemonlist.databinding.ActivityPokemonListBinding
import com.nehak.pokemonlist.utils.LocalLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonListActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityPokemonListBinding
    private lateinit var pokemonListViewModel: PokemonListViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonListBinding.inflate(LayoutInflater.from(this));
        viewBinding.lifecycleOwner = this;
        setContentView(viewBinding.root)

        initViewModel()
        addObservers();

    }

    private fun initViewModel() {
        pokemonListViewModel =
            ViewModelProvider(this).get(PokemonListViewModel::class.java);
    }

    private fun addObservers() {

        lifecycleScope.launch {
            pokemonListViewModel.pokemonList.collect { pokemonList ->
                viewBinding.tvCounts.text = pokemonList?.size.toString()
            }
        }

        lifecycleScope.launch {
            pokemonListViewModel.errorMessage.collect { errorMessage ->
                LocalLog.debug("Received errorMessage $errorMessage")
            }
        }

        lifecycleScope.launch {
            pokemonListViewModel.isLoading.collect { isLoading ->
                LocalLog.debug("Received isLoading $isLoading")
                viewBinding.isLoading = isLoading
            }
        }
    }
}