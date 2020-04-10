package com.silas.themovies.ui.detail.presenter

import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.ui.LoadingState

interface DetailsContract {

    interface View {
        fun updateMovieDetails(movies: Movie)
        fun responseFavorite(movie: Movie?)
        fun updateRelated(pagedRelated: PagedMovies)
        fun updateFavorite(isSuccess: Boolean)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun loadDetails(movieId: Long)
        fun loadRelated(page: Int, movieId: Long)
        fun updateFavorite(vararg movie: Movie)
        fun checkFavoriteId(movieId: Long)
        fun destroy()
    }
}