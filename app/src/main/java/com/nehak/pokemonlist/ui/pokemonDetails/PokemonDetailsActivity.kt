package com.nehak.pokemonlist.ui.pokemonDetails

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import dagger.hilt.android.AndroidEntryPoint
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonDetailsBinding
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.LocalLogs
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {

    private var mErrorSnackBar: Snackbar? = null

    @Inject
    lateinit var viewModelFactory: PokemonDetailsViewModel.AssistedFactory

    @VisibleForTesting
    val viewModel: PokemonDetailsViewModel by viewModels {
        val pokemonModel = intent.getParcelableExtra<Parcelable>(
            EXTRA_POKEMON
        ) as PokemonModel
        PokemonDetailsViewModel.provideFactory(viewModelFactory, pokemonModel)
    }


    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPokemonDetailsBinding.inflate(LayoutInflater.from(this));
        viewBinding.lifecycleOwner = this;
        setContentView(viewBinding.root)
        viewBinding.pokemon = viewModel.pokemonModel
//        viewBinding.toolBar.navigationIcon?.mutate()?.let {
//            it.setTint(Color.WHITE)
//            viewBinding.toolBar.navigationIcon = it
//        }
        addObservers()
    }


    private fun addObservers() {
        lifecycleScope.launch {
            viewModel.errorMessage.collect { errorMessage ->
                if (errorMessage != null) {
                    showErrorWithRetry(errorMessage)
                    LocalLogs.debug("Received errorMessage $errorMessage")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.pokemonDetail.collect { pokemonDetail ->
                viewBinding.pokemonDetail = pokemonDetail
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                viewBinding.isLoading = isLoading
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


}
