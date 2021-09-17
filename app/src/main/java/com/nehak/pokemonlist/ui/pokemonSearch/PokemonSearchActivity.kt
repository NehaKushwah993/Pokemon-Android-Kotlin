package com.nehak.pokemonlist.ui.pokemonSearch

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonSearchBinding
import com.nehak.pokemonlist.ui.pokemonDetails.PokemonDetailsActivity
import com.nehak.pokemonlist.ui.pokemonList.PokemonAdapter
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.interfaces.OnPokemonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


/**
 * Created by Neha Kushwah on 10/9/21.
 */
@AndroidEntryPoint
class PokemonSearchActivity : AppCompatActivity() {

    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonSearchBinding

    @VisibleForTesting
    val viewModel: PokemonSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonSearchBinding.inflate(LayoutInflater.from(this))

        viewBinding.apply {
            lifecycleOwner = this@PokemonSearchActivity;
            pokemonSearchViewModel = this@PokemonSearchActivity.viewModel
            pokemonListAdapter = initAdapter()
        }

        setContentView(viewBinding.root)
        addObservers()
    }

    private fun initAdapter() : PokemonAdapter {
        val adapter = PokemonAdapter()
        adapter.onPokemonClickListener = object : OnPokemonClickListener {
            override fun onPokemonClick(pos: Int, pokemonModel: PokemonModel, commonView: View) {
                val intent = Intent(this@PokemonSearchActivity, PokemonDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_POKEMON, pokemonModel)
                intent.putExtras(bundle)

                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        this@PokemonSearchActivity,
                        commonView,
                        getString(R.string.shared_element_pokemon_image)
                    )
                startActivity(intent, options.toBundle())
            }
        }
        return adapter
    }


    private fun addObservers() {

        lifecycleScope.launch {
            viewModel.pokemonList.collect { pokemonList ->
                viewBinding.pokemonListAdapter?.setPokemonList(pokemonList)
                viewBinding.showCenterError = viewModel.hasFinishedSearch() && pokemonList?.isNullOrEmpty() == true
            }
        }

    }
}