package com.silas.themovies.data.repository.favorites

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.data.sources.local.dao.FavoritesDao
import com.silas.themovies.model.entity.Movie
import io.reactivex.Maybe

class FavoritesRepositoryImpl(private val favoritesDao: FavoritesDao):
    FavoritesRepository {

    override fun loadFavorites(): Maybe<List<Movie>> {
        return favoritesDao.loadFavorites()
    }

    override fun searchFavorites(query: String): Maybe<List<Movie>> {
        return favoritesDao.searchFavorites(query)
    }

}