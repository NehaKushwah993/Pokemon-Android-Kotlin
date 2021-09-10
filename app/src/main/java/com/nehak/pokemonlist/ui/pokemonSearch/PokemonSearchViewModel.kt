package com.nehak.pokemonlist.ui.pokemonSearch

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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Neha Kushwah on 8/9/21.
 */

@VisibleForTesting
@HiltViewModel
class PokemonSearchViewModel @Inject constructor(
    @VisibleForTesting var pokemonRepository: PokemonRepository
) : ViewModel(), LifecycleObserver {

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
    private val searchString: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _pokemonList = searchString.flatMapLatest { str ->
        pokemonRepository.searchPokemonList(
            pokemonName = str,
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
        )
    }
    val pokemonList: Flow<List<PokemonModel>?>
        get() = _pokemonList

    fun setSearchQuery(search: String) {
        this.searchString.value = search
    }

    fun isSearchQueryBlank(): Boolean {
        return searchString.value.isNullOrBlank()
    }

    fun research() {
        viewModelScope.launch {
            val oldValue = searchString.value
            searchString.emit("");
            searchString.emit(oldValue)
        }
    }

}