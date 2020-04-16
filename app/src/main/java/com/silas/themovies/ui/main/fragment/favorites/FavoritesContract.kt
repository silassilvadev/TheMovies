package com.silas.themovies.ui.main.fragment.favorites

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.LoadingState

interface FavoritesContract {

    interface View {
        fun updateFavorites(movies: ArrayList<Movie>)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun loadMovies(query: String = "")
        fun destroy()
    }
}