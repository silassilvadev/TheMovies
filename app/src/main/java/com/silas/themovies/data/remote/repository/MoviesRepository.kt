package com.silas.themovies.data.remote.repository

import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto

/**
 * You receive a request, request data and decide where, and how to get the request.
 *
 * @param service Single Service instance that will set up the request,
 * ask the Client to initialize the request and will listen to the request return to return to the caller
 * @param moviesDao Single instance of Dao that assembles a query,
 * asked a Database for the necessary data and will be listening to your response to return it
 *
 * @author Silas at 22/02/2020
 */
class MoviesRepository(private val service: MoviesService, private val moviesDao: MoviesDao) {

    fun loadPopulars(pagedListMoviesDto: PagedListMoviesDto) =
        service.loadPopulars(
            pagedListMoviesDto.apiKey,
            pagedListMoviesDto.language,
            pagedListMoviesDto.page)


    fun searchPopulars(pagedListMovies: PagedListMoviesDto) =
        service.searchPopulars(
            pagedListMovies.apiKey,
            pagedListMovies.language,
            pagedListMovies.page,
            pagedListMovies.search ?: "")

    fun loadDetails(movieDetails: MovieDetailsDto) =
        service.loadDetails(
            movieDetails.idMovie,
            movieDetails.apiKey,
            movieDetails.language)

    fun loadRelated(pagedListMovies: PagedListMoviesDto) =
        service.searchRelated(
            pagedListMovies.movieId,
            pagedListMovies.apiKey,
            pagedListMovies.language,
            pagedListMovies.page)


    fun insertFavorite(vararg movie: Movie) = moviesDao.insertFavorite(*movie)

    fun deleteFavorite(vararg movie: Movie)  = moviesDao.deleteFavorite(*movie)

    fun checkFavoriteId(movieId: Long)  = moviesDao.checkFavoriteId(movieId)

    fun loadFavorites() = moviesDao.loadFavorites()

    fun searchFavorites(query: String)  = moviesDao.searchFavorites(query)
}