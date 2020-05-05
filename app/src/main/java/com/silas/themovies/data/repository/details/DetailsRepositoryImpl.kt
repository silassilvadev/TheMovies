package com.silas.themovies.data.repository.details

import com.silas.themovies.data.source.local.dao.FavoritesDao
import com.silas.themovies.data.source.remote.service.DetailsService
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single

class DetailsRepositoryImpl(private val detailsService: DetailsService,
                            private val favoritesDao: FavoritesDao,
                            private val apiKey: String,
                            private val language: String): DetailsRepository {

    override fun loadDetails(movieId: Long) = detailsService.loadDetails(movieId, apiKey, language)

    override fun loadRelated(page: Int, movieId: Long): Single<PagedMovies> {
        return detailsService.loadRelated(movieId, apiKey, language, page)
    }

    override fun insertFavorite(vararg movie: Movie) = favoritesDao.insertFavorite(*movie)

    override fun deleteFavorite(vararg movie: Movie) = favoritesDao.deleteFavorite(*movie)

    override fun checkFavoriteId(movieId: Long) = favoritesDao.checkFavoriteId(movieId)

}