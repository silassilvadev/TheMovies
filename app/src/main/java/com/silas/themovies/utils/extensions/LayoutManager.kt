package com.silas.themovies.utils.extensions

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.silas.themovies.model.dto.response.PagedMovies

/**
 * Checks whether list needs to be paged
 *
 * @param stateRecyclerView Rolling state of RecyclerView
 * @param pagedMovies List to be checked
 * @return true if pagination is necessary otherwise false
 * Note: Implementation for LinearLayoutManager
 */
fun LinearLayoutManager.isPaginationNecessary(stateRecyclerView: Int,
                                              pagedMovies: PagedMovies): Boolean {
    //Was used so that the list is updated only when the scrolling ends and avoid 2 simultaneous calls
    return stateRecyclerView == RecyclerView.SCROLL_STATE_IDLE
            && pagedMovies.page < pagedMovies.totalPages
            && itemCount <= pagedMovies.totalResults
            && findLastVisibleItemPosition() > (itemCount - 2)
}

/**
 * Similar option for GridLayoutManager
 */
fun GridLayoutManager.isPaginationNecessary(stateRecyclerView: Int,
                                            pagedMovies: PagedMovies): Boolean {
    return (this as? LinearLayoutManager)?.isPaginationNecessary(stateRecyclerView, pagedMovies) ?: false
}
