package com.nehak.pokemonlist.utils

import com.nehak.pokemonlist.BuildConfig

/**
 * Created by Neha Kushwah on 8/9/21.
 */
class LocalLogs {
    companion object {
        fun debug(s: String) {
            if (BuildConfig.DEBUG) {
                print(s)
            }
        }

    }
}