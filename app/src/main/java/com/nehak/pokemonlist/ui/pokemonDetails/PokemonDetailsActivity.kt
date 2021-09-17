package com.nehak.pokemonlist.ui.pokemonDetails

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.models.PokemonModel
import com.nehak.pokemonlist.databinding.ActivityPokemonDetailsBinding
import com.nehak.pokemonlist.utils.EXTRA_POKEMON
import com.nehak.pokemonlist.utils.LocalLogs
import com.nehak.pokemonlist.utils.interfaces.OnError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Created by Neha Kushwah on 7/9/21.
 */
@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: PokemonDetailsViewModel.AssistedFactory

    @VisibleForTesting
    lateinit var viewBinding: ActivityPokemonDetailsBinding

    @VisibleForTesting
    val viewModel: PokemonDetailsViewModel by viewModels {
        val pokemonModel = intent.getParcelableExtra<Parcelable>(
            EXTRA_POKEMON
        ) as PokemonModel
        PokemonDetailsViewModel.provideFactory(viewModelFactory, pokemonModel)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPokemonDetailsBinding.inflate(LayoutInflater.from(this))
        viewBinding.apply {
            lifecycleOwner = this@PokemonDetailsActivity
            pokemonDetailViewModel = this@PokemonDetailsActivity.viewModel
            pokemon = viewModel.pokemonModel
            onDetailsError = object : OnError {
                override fun onError(errorMessage: String?) {
                    showErrorWithRetry(errorMessage)
                    LocalLogs.debug("Received errorMessage $errorMessage")
                }
            }
            onBackClickListener = View.OnClickListener {
                supportFinishAfterTransition()
            }
        }
        setContentView(viewBinding.root)
    }

    /**
     * Show snackBar with retry button
     */
    @VisibleForTesting
    private fun showErrorWithRetry(errorMessage: String?) {
        errorMessage?.let {
            val mErrorSnackBar =
                Snackbar.make(
                    viewBinding.root,
                    it,
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
