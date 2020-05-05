package com.silas.themovies.ui.detail

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.model.state.LoadingState

interface DetailsContract {

    interface View {
        fun updateMovieDetails(updatedMovie: Movie)
        fun isFavorite(isFavorite: Boolean)
        fun updateRelated(pagedRelated: PagedMovies)
        fun updateFavorite(isFavorite: Boolean)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun loadDetails(movieId: Long)
        fun loadRelated(movieId: Long, isNextPage: Boolean = false)
        fun updateFavorite(movie: Movie)
        fun checkIsFavorite(movieId: Long)
        fun destroy()
    }
}