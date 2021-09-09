package com.nehak.pokemonlist.utils.bindingAdapters

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_PREFIX
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_SUFFIX
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadPokemonImage(imageView: ImageView, url: String) {
        if (!TextUtils.isEmpty(url)) {

            val index = url.split("/".toRegex()).dropLast(1).last()
            val imageUrl = IMAGE_POKEMON_PREFIX + index + IMAGE_POKEMON_SUFFIX;
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

            Glide.with(imageView)
                .load(imageUrl)
                .transition(GenericTransitionOptions.with(R.anim.zoom_in))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_pokemon)
                .into(imageView)
        }
    }

}