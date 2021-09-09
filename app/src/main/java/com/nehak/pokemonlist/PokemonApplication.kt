package com.nehak.pokemonlist

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Created by Neha Kushwah on 7/9/21.
 */
@HiltAndroidApp
class PokemonApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}