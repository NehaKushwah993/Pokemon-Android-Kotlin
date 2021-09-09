package com.nehak.pokemonlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.nehak.pokemonlist.databinding.ActivityPokemonListBinding
import com.nehak.pokemonlist.utils.LocalLogs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.utils.interfaces.OnItemClickListener
import com.nehak.pokemonlist.utils.recyclerViewPagination.PaginationListener


/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonListActivity : AppCompatActivity() {

    private var mSnackbar: Snackbar? = null

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

    }

    private fun initAdapter() {
        val adapter = PokemonAdapter()
        viewBinding.pokemonAdapter = adapter
        adapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(pos: Int) {
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
                if(!isLoading) {
                    viewBinding.swipeRefreshLayout.isRefreshing = isLoading
                }else{
                    mSnackbar?.dismiss()
                }
            }
        }
    }

    /**
     * Show snackBar with retry button
     */
    @VisibleForTesting
    private fun showErrorWithRetry(msg: String?) {
        mSnackbar =
            Snackbar.make(
                viewBinding.root,
                msg ?: getString(R.string.error_message),
                Snackbar.LENGTH_INDEFINITE
            )
        mSnackbar?.setAction(getString(R.string.retry).uppercase()) {
            mSnackbar?.dismiss()
            viewModel.reload()
        }
        mSnackbar?.show()
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

            override fun isLastPage() : Boolean{
                return !viewModel.shouldLoadForNextPage()
            }

            override fun isLoading() : Boolean{
                return viewModel.isLoading.value
            }
        })
    }
}