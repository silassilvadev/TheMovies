package com.silas.themovies.data.repository.details

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single

interface DetailsRepository {
    fun loadDetails(movieId: Long): Single<Movie>
    fun loadRelated(page: Int, movieId: Long): Single<PagedMovies>
    fun checkFavoriteId(movieId: Long): Single<Movie?>
    fun insertFavorite(vararg movie: Movie): Single<List<Long>>
    fun deleteFavorite(vararg movie: Movie): Single<Int>
}