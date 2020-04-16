package com.silas.themovies.data.repository.details

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Maybe
import io.reactivex.Single

interface DetailsRepository {
    fun loadDetails(movieId: Long): Single<Movie>
    fun loadRelated(page: Int, movieId: Long): Single<PagedMovies>
    fun checkFavoriteId(movieId: Long): Maybe<Movie?>
    fun insertFavorite(vararg movie: Movie): Maybe<List<Long>>
    fun deleteFavorite(vararg movie: Movie): Maybe<Int>
}