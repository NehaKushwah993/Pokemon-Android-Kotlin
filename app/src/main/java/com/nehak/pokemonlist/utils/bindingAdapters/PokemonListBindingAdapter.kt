package com.nehak.pokemonlist.utils.bindingAdapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.ui.pokemonList.PokemonAdapter
import com.nehak.pokemonlist.ui.pokemonList.PokemonListViewModel
import com.nehak.pokemonlist.utils.recyclerViewPagination.PaginationListener
import kotlinx.coroutines.flow.collect

object PokemonListBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["setRefreshing"],
    )
    fun setRefreshing(
        swipeRefreshLayout: SwipeRefreshLayout,
        isLoading: Boolean,
    ) {
        if (!isLoading) {
            swipeRefreshLayout.isRefreshing = isLoading
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["addPagination"]
    )
    fun addPagination(
        recyclerView: RecyclerView,
        viewModel: PokemonListViewModel,
    ) {
        recyclerView.addOnScrollListener(object :
            PaginationListener(
                recyclerView.layoutManager as GridLayoutManager,
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