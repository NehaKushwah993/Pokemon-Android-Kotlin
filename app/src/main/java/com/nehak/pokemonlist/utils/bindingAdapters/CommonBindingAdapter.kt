package com.nehak.pokemonlist.utils.bindingAdapters

import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.ObjectKey

import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_PREFIX
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_SUFFIX

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadPokemonImage(imageView: ImageView, url: String) {
        if (!TextUtils.isEmpty(url)) {

            val index = url.split("/".toRegex()).dropLast(1).last()
            val imageUrl = IMAGE_POKEMON_PREFIX + index + IMAGE_POKEMON_SUFFIX;

            Glide.with(imageView)
                .load(imageUrl)
                .transition(GenericTransitionOptions.with(R.anim.zoom_in))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(index))
                .placeholder(R.drawable.ic_pokemon)
                .into(imageView)
        }
    }

}