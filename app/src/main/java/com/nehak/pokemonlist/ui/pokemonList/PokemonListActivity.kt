package com.nehak.pokemonlist.ui.pokemonList

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonListBinding
import com.nehak.pokemonlist.ui.pokemonDetails.PokemonDetailsActivity
import com.nehak.pokemonlist.ui.pokemonSearch.PokemonSearchActivity
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.LocalLogs
import com.nehak.pokemonlist.utils.interfaces.OnPokemonClickListener
import com.nehak.pokemonlist.utils.recyclerViewPagination.PaginationListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonListActivity : AppCompatActivity() {

    private var mErrorSnackBar: Snackbar? = null

    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonListBinding

    @VisibleForTesting
    lateinit var viewModel: PokemonListViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPokemonListBinding.inflate(LayoutInflater.from(this));
        viewBinding.lifecycleOwner = this;
        setContentView(viewBinding.root)

        initViewModel()
        initAdapter()
        initPullToRefresh()
        addObservers();
        addPagination()
        addClickListeners()

    }

    private fun addClickListeners() {

        viewBinding.fabSearch.setOnClickListener {
            val intent = Intent(this@PokemonListActivity, PokemonSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initAdapter() {
        val adapter = PokemonAdapter()
        viewBinding.pokemonAdapter = adapter
        adapter.onPokemonClickListener = object : OnPokemonClickListener {
            override fun onPokemonClick(pos: Int, pokemonModel: PokemonModel, commonView: View) {

                val intent = Intent(this@PokemonListActivity, PokemonDetailsActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable(EXTRA_POKEMON, pokemonModel)
                intent.putExtras(bundle);

                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        this@PokemonListActivity,
                        commonView,
                        getString(R.string.shared_element_pokemon_image)
                    )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PokemonListViewModel::class.java);
    }

    private fun initPullToRefresh() {
        viewBinding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun addObservers() {

        lifecycleScope.launch {
            viewModel.pokemonList.collect { pokemonList ->
                viewBinding.pokemonAdapter?.setPokemonList(pokemonList)
            }
        }

        lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                if (errorMessage != null) {
                    showErrorWithRetry(errorMessage)
                    LocalLogs.debug("Received errorMessage $errorMessage")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                LocalLogs.debug("Received isLoading $isLoading")
                viewBinding.isLoading = isLoading
                if (!isLoading) {
                    viewBinding.swipeRefreshLayout.isRefreshing = isLoading
                } else {
                    mErrorSnackBar?.dismiss()
                }
            }
        }
    }

    /**
     * Show snackBar with retry button
     */
    @VisibleForTesting
    private fun showErrorWithRetry(msg: String?) {
        mErrorSnackBar =
            Snackbar.make(
                viewBinding.root,
                msg ?: getString(R.string.error_message),
                Snackbar.LENGTH_INDEFINITE
            )
        mErrorSnackBar?.setAction(getString(R.string.retry).uppercase()) {
            mErrorSnackBar?.dismiss()
            viewModel.reload()
        }
        mErrorSnackBar?.show()
    }

    /**
     * add scroll listener while user reach in bottom load more will call
     */
    private fun addPagination() {
        viewBinding.rvPokemonList.addOnScrollListener(object :
            PaginationListener(
                viewBinding.rvPokemonList.layoutManager as GridLayoutManager,
                viewModel.paginationThreshold
            ) {

            override fun loadMoreItems() {
                viewModel.fetchMorePokemon()
            }

            override fun isLastPage(): Boolean {
                return !viewModel.shouldLoadForNextPage()
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading.value
            }
        })
    }
}