
package com.nehak.pokemonlist.ui.pokemonList

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonListBinding
import com.nehak.pokemonlist.ui.pokemonDetails.PokemonDetailsActivity
import com.nehak.pokemonlist.ui.pokemonSearch.PokemonSearchActivity
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.interfaces.OnError
import com.nehak.pokemonlist.utils.interfaces.OnPokemonClickListener
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

    @VisibleForTesting
    val viewModel: PokemonListViewModel by viewModels()

    private val launchSearchActivity = View.OnClickListener {
        val intent = Intent(this@PokemonListActivity, PokemonSearchActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonListBinding.inflate(LayoutInflater.from(this))
        viewBinding.apply {
            lifecycleOwner = this@PokemonListActivity
            viewModel = this@PokemonListActivity.viewModel
            searchClickListener = launchSearchActivity
            pokemonAdapter = getAdapter()
            onSwipeRefresh = SwipeRefreshLayout.OnRefreshListener {
                this@PokemonListActivity.viewModel.refresh()
            }
            onError = object : OnError {
                override fun onError(errorMessage: String?) {
                    showErrorWithRetry(errorMessage)
                }
            }
        }
        setContentView(viewBinding.root)
        addObservers()
    }

    private fun getAdapter(): PokemonAdapter {
        val adapter = PokemonAdapter()
        object : OnPokemonClickListener {
            override fun onPokemonClick(pos: Int, pokemonModel: PokemonModel, commonView: View) {
                val intent = Intent(this@PokemonListActivity, PokemonDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_POKEMON, pokemonModel)
                intent.putExtras(bundle)

                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        this@PokemonListActivity,
                        commonView,
                        getString(R.string.shared_element_pokemon_image)
                    )
                startActivity(intent, options.toBundle())
            }
        }.also { adapter.onPokemonClickListener = it }
        return adapter;
    }

    private fun addObservers() {
        lifecycleScope.launch {
            viewModel.pokemonList.collect { pokemonList ->
                viewBinding.pokemonAdapter?.setPokemonList(pokemonList)
                viewBinding.executePendingBindings()
            }
        }
    }

    /**
     * Show snackBar with retry button
     */
    @VisibleForTesting
    private fun showErrorWithRetry(msg: String?) {
        msg?.let {
            val mErrorSnackBar = Snackbar.make(
                viewBinding.root,
                msg ?: getString(R.string.error_message),
                Snackbar.LENGTH_INDEFINITE
            )
            mErrorSnackBar.setAction(getString(R.string.retry).uppercase()) {
                mErrorSnackBar.dismiss()
                viewModel.reload()
            }
            mErrorSnackBar.show()
        }
    }
}