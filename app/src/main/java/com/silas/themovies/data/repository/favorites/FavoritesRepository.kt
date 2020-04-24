package com.silas.themovies.data.repository.favorites

import com.silas.themovies.model.entity.Movie
import io.reactivex.Single


interface FavoritesRepository {
    fun loadFavorites(): Single<List<Movie>>
    fun searchFavorites(query: String): Single<List<Movie>>
}