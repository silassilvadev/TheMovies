package com.silas.themovies.data.repository.movies

import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single

interface MoviesRepository {
    fun loadPopulars(page: Int): Single<PagedMovies>
    fun searchMovies(page: Int, query: String): Single<PagedMovies>
}