package com.nehak.pokemonlist.utils.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nehak.pokemonlist.utils.LocalLogs
import com.nehak.pokemonlist.utils.interfaces.OnError


object CommonBindingAdapter {

    @JvmStatic
    @BindingAdapter(
        value = ["setOnSwipeViewListener"]
    )
    fun setOnSwipeViewListener(
        swipeRefreshLayout: SwipeRefreshLayout,
        onSwipeRefresh: SwipeRefreshLayout.OnRefreshListener?,
    ) {
        swipeRefreshLayout.setOnRefreshListener { onSwipeRefresh?.onRefresh() }
    }

    @JvmStatic
    @BindingAdapter(
        value = ["onError", "errorMessage"]
    )
    fun onError(
        view: View,
        onError: OnError?,
        errorMessage: String?,
    ) {
        if (errorMessage != null) {
            onError?.onError(errorMessage)
            LocalLogs.debug("Received errorMessage $errorMessage")
        }
    }

}