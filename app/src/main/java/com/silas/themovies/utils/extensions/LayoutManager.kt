package com.silas.themovies.utils.extensions

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.model.dto.response.PagedListMovies

/**
 * [RecyclerView.SCROLL_STATE_IDLE] was used so that the list is updated
 * only when the scrolling ends and avoid 2 simultaneous calls
 */
fun LinearLayoutManager.isPaginationNecessary(stateRecyclerView: Int,
                                              pagedListMovies: PagedListMovies): Boolean {
    return stateRecyclerView == RecyclerView.SCROLL_STATE_IDLE
            && pagedListMovies.page < pagedListMovies.totalPages
            && itemCount <= pagedListMovies.totalResults
            && findLastVisibleItemPosition() > (itemCount - 2)
}