package com.nehak.pokemonlist.utils.bindingAdapters

import android.text.TextUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_PREFIX
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_SUFFIX

object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter("loadImageByPokemonId")
    fun loadImageByPokemonId(imageView: ImageView, id: Int) {
        val imageUrl = IMAGE_POKEMON_PREFIX + id + IMAGE_POKEMON_SUFFIX;
        Glide.with(imageView)
            .load(imageUrl)
            .transition(GenericTransitionOptions.with(R.anim.zoom_in))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(id))
            .placeholder(R.drawable.ic_pokemon)
            .into(imageView)
    }

}