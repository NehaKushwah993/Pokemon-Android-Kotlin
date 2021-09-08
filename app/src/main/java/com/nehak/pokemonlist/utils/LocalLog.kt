package com.nehak.pokemonlist.utils

import com.nehak.pokemonlist.BuildConfig

/**
 * Created by Neha Kushwah on 8/9/21.
 */
class LocalLog {
    companion object {
        fun debug(s: String) {
            if (BuildConfig.DEBUG) {
                print(s)
            }
        }

        fun debug(tag: String, s: String) {
            if (BuildConfig.DEBUG) {
                print(tag + " - " + s)
            }
        }

    }
}