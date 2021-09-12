package com.nehak.pokemonlist.ui.pokemonSearch

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SearchView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonSearchBinding
import com.nehak.pokemonlist.ui.pokemonDetails.PokemonDetailsActivity
import com.nehak.pokemonlist.ui.pokemonList.PokemonAdapter
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.LocalLogs
import com.nehak.pokemonlist.utils.interfaces.OnPokemonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Neha Kushwah on 10/9/21.
 */
@AndroidEntryPoint
class PokemonSearchActivity : AppCompatActivity() {

    private lateinit var adapter: PokemonAdapter
    private var mErrorSnackBar: Snackbar? = null

    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonSearchBinding

    @VisibleForTesting
    lateinit var viewModel: PokemonSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonSearchBinding.inflate(LayoutInflater.from(this))
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)

        initViewModel()
        initAdapter()
        addObservers()
        initListeners()

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PokemonSearchViewModel::class.java)
    }

    private fun initAdapter() {
        adapter = PokemonAdapter()
        viewBinding.pokemonAdapter = adapter
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
    }


    private fun addObservers() {

        lifecycleScope.launch {
            viewModel.pokemonList.collect { pokemonList ->
                viewBinding.pokemonAdapter?.setPokemonList(pokemonList)
                checkError()
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                checkError()
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                LocalLogs.debug("Received isLoading $isLoading")
                viewBinding.isLoading = isLoading
                if (isLoading) {
                    mErrorSnackBar?.dismiss()
                }
                checkError()
            }
        }
    }

    private fun initListeners() {
        viewBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.setSearchQuery(it) }
                return true
            }
        }
        )
    }


    private fun checkError() {
        viewBinding.foundNoData =
            !viewModel.isLoading.value
                    && !viewModel.isSearchQueryBlank()
                    && adapter.itemCount == 0
    }

}