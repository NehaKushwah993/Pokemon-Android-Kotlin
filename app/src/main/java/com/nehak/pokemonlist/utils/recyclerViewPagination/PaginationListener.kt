package com.nehak.pokemonlist.utils.recyclerViewPagination

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.nehak.pokemonlist.utils.recyclerViewPagination.PaginationListener

abstract class PaginationListener : RecyclerView.OnScrollListener {
    private var threshold: Int = 10;
    private val layoutManager: RecyclerView.LayoutManager

    constructor(layoutManager: LinearLayoutManager, pageSize: Int) {
        this.layoutManager = layoutManager
        this.threshold = threshold
    }

    constructor(layoutManager: GridLayoutManager, pageSize: Int) {
        this.layoutManager = layoutManager
        this.threshold = threshold
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        var firstVisibleItemPosition = 0
        if (layoutManager is GridLayoutManager) {
            firstVisibleItemPosition =
                layoutManager.findFirstVisibleItemPosition()
        } else if (layoutManager is LinearLayoutManager) {
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        }
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= threshold) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

}