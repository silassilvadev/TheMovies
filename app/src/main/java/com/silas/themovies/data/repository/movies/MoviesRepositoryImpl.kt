package com.silas.themovies.data.repository.movies

import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.data.sources.remote.service.MoviesService
import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single

class MoviesRepositoryImpl(private val moviesService: MoviesService,
                           private val apiKey: String,
                           private val language: String):
    MoviesRepository {

    override fun loadPopulars(page: Int): Single<PagedMovies> {
        return moviesService.loadPopulars(apiKey, language, page)
    }

    override fun searchMovies(page: Int, query: String): Single<PagedMovies> {
        return moviesService.searchMovies(apiKey, language, page, query)
    }

}