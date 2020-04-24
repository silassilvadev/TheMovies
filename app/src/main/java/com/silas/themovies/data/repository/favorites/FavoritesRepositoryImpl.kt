package com.silas.themovies.data.repository.favorites

import com.silas.themovies.data.sources.local.dao.FavoritesDao
import com.silas.themovies.model.entity.Movie
import io.reactivex.Single

class FavoritesRepositoryImpl(private val favoritesDao: FavoritesDao):
    FavoritesRepository {

    override fun loadFavorites(): Single<List<Movie>> {
        return favoritesDao.loadFavorites()
    }

    override fun searchFavorites(query: String): Single<List<Movie>> {
        return favoritesDao.searchFavorites(query)
    }

}