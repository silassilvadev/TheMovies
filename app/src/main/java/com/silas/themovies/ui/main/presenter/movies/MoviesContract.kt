package com.silas.themovies.ui.main.presenter.movies

import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.ui.LoadingState

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