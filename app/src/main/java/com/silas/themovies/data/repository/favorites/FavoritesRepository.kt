package com.silas.themovies.data.repository.favorites

import com.silas.themovies.model.entity.Movie
import io.reactivex.Maybe

interface FavoritesRepository {
    fun loadFavorites(): Maybe<List<Movie>>
    fun searchFavorites(query: String): Maybe<List<Movie>>
}