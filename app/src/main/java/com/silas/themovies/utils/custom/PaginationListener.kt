package com.silas.themovies.utils.custom

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationListener(private var totalResults: Int,
                         private val onPagination: () -> Unit): RecyclerView.OnScrollListener() {

    var isLoadingEnable = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        (recyclerView.layoutManager as? GridLayoutManager)?.apply {

            if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadingEnable) {
                if (itemCount <= totalResults && findLastVisibleItemPosition() > (itemCount - 2)) {
                    onPagination.invoke()
                }
            }
        }
    }
}