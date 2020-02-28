package com.silas.themovies.data.remote.repository

import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.MovieDetails
import com.silas.themovies.model.dto.response.PagedListMovies

class MoviesRepository(private val service: MoviesService, private val moviesDao: MoviesDao) {

    suspend fun loadPopulars(pagedListMoviesDto: PagedListMoviesDto): PagedListMovies {
        return service.loadPopulars(
            pagedListMoviesDto.apiKey,
            pagedListMoviesDto.language,
            pagedListMoviesDto.page)
    }

    suspend fun searchMovie(pagedListMovies: PagedListMoviesDto): PagedListMovies {
        return service.searchMovie(
            pagedListMovies.apiKey,
            pagedListMovies.language,
            pagedListMovies.page,
            pagedListMovies.search ?: "")
    }

    suspend fun searchRelated(pagedListMovies: PagedListMoviesDto): PagedListMovies {
        return service.searchRelated(
            pagedListMovies.movieId,
            pagedListMovies.apiKey,
            pagedListMovies.language,
            pagedListMovies.page)
    }

    suspend fun loadDetails(movieDetails: MovieDetailsDto): MovieDetails {
        return service.loadDetails(
            movieDetails.idMovie,
            movieDetails.apiKey,
            movieDetails.language)
    }

    suspend fun insertFavorite(vararg movie: Movie) = moviesDao.insertFavorite(*movie)

    suspend fun deleteFavorite(vararg movie: Movie) = moviesDao.deleteFavorite(*movie)

    suspend fun loadFavoriteId(movieId: Long) = moviesDao.loadFavoriteId(movieId)

    suspend fun loadFavorites() = moviesDao.loadFavorites()

    suspend fun searchFavorites(query: String) = moviesDao.searchFavorites(query)
}