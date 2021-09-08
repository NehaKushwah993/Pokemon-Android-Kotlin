package com.nehak.pokemonlist.utils

import android.content.Context
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Created by Neha Kushwah on 8/9/21.
 */
class Utils {
    companion object {

        fun loadJSONFromAsset(context: Context, str: String?): String? {
            var json: String? = null
            try {
                val `is` = context.assets.open(str!!)
                val size = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                json = String(buffer, StandardCharsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            return json
        }

    }
}