package com.silas.themovies.ui.main.presenter.favorites

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.LoadingState

interface FavoritesContract {

    interface View {
        fun updateFavorites(movies: ArrayList<Movie>)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun loadFavorites(query: String = "")
        fun destroy()
    }
}