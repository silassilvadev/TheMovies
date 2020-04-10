package com.silas.themovies.ui.main.presenter

import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.ui.LoadingState

interface MoviesContract {

    interface View {
        fun updateMovies(pagedMovies: PagedMovies)
        fun responseError(message: String)
        fun updateLoading(state: LoadingState)
    }

    interface Presenter {
        fun getPopulars(page: Int = 1, query: String = "")
        fun getFavorites(query: String = "")
        fun destroy()
    }

}