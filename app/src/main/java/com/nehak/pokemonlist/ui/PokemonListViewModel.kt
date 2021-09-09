package com.nehak.pokemonlist.ui

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val limit = 10

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
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            pokemonRepository.fetchPokemonList(
                limit = limit,
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

    fun reload() {
        fetchBooks();
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

}