package com.nehak.pokemonlist.ui.pokemonList

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.repository.PokemonRepository
import com.nehak.pokemonlist.utils.MAX_LIST_SIZE
import com.nehak.pokemonlist.utils.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 8/9/21.
 */

@VisibleForTesting
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    @VisibleForTesting var pokemonRepository: PokemonRepository
) : ViewModel(), LifecycleObserver {

    private val pageSize = PAGE_SIZE
    val paginationThreshold = pageSize - 4
    var currentPageNumber: Int = 0

    // To show loading bar
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    // To show error message
    private val _errorMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    val errorMessage: StateFlow<String?>
        get() = _errorMessage

    // List to show Pokemon's
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val _pokemonList: MutableStateFlow<List<PokemonModel>?> = MutableStateFlow(null)
    val pokemonList: Flow<List<PokemonModel>?>
        get() = _pokemonList

    init {
        fetchPokemons()
    }

    private fun fetchPokemons() {
        viewModelScope.launch {
            pokemonRepository.fetchPokemonList(
                pageNumber = currentPageNumber,
                limit = pageSize,
                onStart = {
                    _isLoading.value = true
                    _errorMessage.value = null
                },
                onComplete = {
                    _isLoading.value = false
                },
                onError = {
                    _errorMessage.value = it
                }
            ).collect {
                _pokemonList.emit(it);
            }
        }
    }

    /**
     * Refetch data from server
     */
    fun reload() {
        fetchPokemons();
    }

    /**
     * Refetch data from server after deleting them from DB
     */
    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepository.clear().also {
                viewModelScope.launch(Dispatchers.Main) {
                    resetVariables()
                    fetchPokemons();
                }
            }
        }
    }

    private fun resetVariables() {
        _pokemonList.value = null
        currentPageNumber = 0
    }

    fun fetchMorePokemon() {
        ++currentPageNumber
        fetchPokemons()
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setErrorMessage(msg: String?) {
        _errorMessage.value = msg
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setPokemonList(pokemonList: List<PokemonModel>?) {
        _pokemonList.value = pokemonList
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun clearPokemonList() {
        viewModelScope.launch {
            _pokemonList.emit(null);
        }
    }

    /**
     * Return true if the last fetched pokemon list had next page URL
     * &
     * We have not reached 300 items
     */
    fun shouldLoadForNextPage(): Boolean {
        if (_pokemonList.value != null && _pokemonList.value!!.isNotEmpty()) {
            return _pokemonList.value!!.last().hasNextPageUrl && _pokemonList.value?.size!! < MAX_LIST_SIZE
        }
        return false;
    }

}