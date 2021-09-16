package com.nehak.pokemonlist.utils.bindingAdapters

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.palette.graphics.Palette
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


object ImageBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["loadImageByPokemonId",
            "paletteView",
            "animate",
            "statusBarColorUpdate"],
        requireAll = false
    )
    fun loadImageByPokemonId(
        imageView: ImageView,
        id: Int,
        paletteView: View?,
        animate: Boolean?,
        statusBarColorUpdate: Boolean?
    ) {
        val shouldAnimate = animate ?: false
        val imageUrl = IMAGE_POKEMON_PREFIX + id + IMAGE_POKEMON_SUFFIX
        var requestBuilder = Glide.with(imageView)
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
                    if (paletteView != null) {
                        val bitmap = (resource as BitmapDrawable).bitmap
                        Palette.from(bitmap).generate {
                            if (it != null) {
                                paletteView.setBackgroundColor(
                                    it.getMutedColor(Color.TRANSPARENT)
                                )
                                if (statusBarColorUpdate != null && statusBarColorUpdate) {
                                    val context = paletteView.context
                                    if (context is AppCompatActivity) {
                                        context.window.apply {
                                            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                                            statusBarColor = it.getDarkMutedColor(Color.TRANSPARENT)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return false
                }
            })
        if (shouldAnimate) {
            requestBuilder =
                requestBuilder.transition(GenericTransitionOptions.with(R.anim.zoom_in))
        }
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(id))
            .into(imageView)
    }

}