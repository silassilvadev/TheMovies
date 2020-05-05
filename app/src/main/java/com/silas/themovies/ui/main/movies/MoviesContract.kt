package com.silas.themovies.ui.main.movies

import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.model.state.LoadingState

interface MoviesContract {

    interface View {
        fun updateMovies(pagedMovies: PagedMovies)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun loadMovies(query: String = "", isNextPage: Boolean = false)
        fun destroy()
    }
}