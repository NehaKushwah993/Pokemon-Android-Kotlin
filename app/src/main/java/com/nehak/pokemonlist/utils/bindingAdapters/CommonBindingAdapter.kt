package com.nehak.pokemonlist.utils.bindingAdapters

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.PaletteAsyncListener
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.nehak.pokemonlist.R
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_PREFIX
import com.nehak.pokemonlist.backend.network.IMAGE_POKEMON_SUFFIX


object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter(value = ["loadImageByPokemonId", "paletteCard"], requireAll = false)
    fun loadImageByPokemonId(imageView: ImageView, id: Int, view: View?) {
        val imageUrl = IMAGE_POKEMON_PREFIX + id + IMAGE_POKEMON_SUFFIX;
        Glide.with(imageView)
            .load(imageUrl)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (view != null) {
                        val bitmap = (resource as BitmapDrawable).bitmap
                        Palette.from(bitmap).generate(PaletteAsyncListener {
                            // Use generated instance
                            if (it != null) {
                                view.setBackgroundColor(it.getLightVibrantColor(Color.TRANSPARENT))
                            }
                        })
                    }
                    return false
                }

            })
            .transition(GenericTransitionOptions.with(R.anim.zoom_in))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(id))
            .into(imageView)
    }

}