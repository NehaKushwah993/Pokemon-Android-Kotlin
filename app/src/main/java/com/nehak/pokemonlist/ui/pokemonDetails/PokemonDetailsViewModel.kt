package com.nehak.pokemonlist.ui.pokemonDetails

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.backend.models.pokemonDetails.PokemonDetails
import com.nehak.pokemonlist.backend.repository.PokemonRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by Neha Kushwah on 8/9/21.
 */
@VisibleForTesting
class PokemonDetailsViewModel @AssistedInject constructor(
    @VisibleForTesting var pokemonRepository: PokemonRepository,
    @Assisted val pokemonModel: PokemonModel
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
    private val _pokemonDetail: MutableStateFlow<PokemonDetails?> = MutableStateFlow(null)
    val pokemonDetail: Flow<PokemonDetails?>
        get() = _pokemonDetail

    init {
        fetchPokemonDetails()
    }

    private fun fetchPokemonDetails() {
        viewModelScope.launch {
            pokemonRepository.fetchPokemonDetail(
                pokemonName = pokemonModel.name,
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
                _pokemonDetail.emit(it)
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setErrorMessage(msg: String?) {
        _errorMessage.value = msg
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setPokemonDetail(pokemonDetails: PokemonDetails?) {
        _pokemonDetail.value = pokemonDetails
    }

    fun reload() {
        fetchPokemonDetails()
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pokemonModel: PokemonModel): PokemonDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            pokemonModel: PokemonModel
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(pokemonModel) as T
            }
        }
    }
}