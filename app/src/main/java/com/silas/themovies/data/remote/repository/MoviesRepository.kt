package com.silas.themovies.data.remote.repository

import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.PagedMovies
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

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

    suspend fun loadDetailsAsync(movieDetails: MovieDetailsDto) =
        withContext(Dispatchers.IO) {
            async {
                service.loadDetails(
                    movieDetails.idMovie,
                    movieDetails.apiKey,
                    movieDetails.language)
            }
        }

    suspend fun loadRelatedAsync(pagedListMovies: PagedListMoviesDto) =
        withContext(Dispatchers.IO) {
            async {
                service.searchRelated(
                    pagedListMovies.movieId,
                    pagedListMovies.apiKey,
                    pagedListMovies.language,
                    pagedListMovies.page
                )
            }
        }

    suspend fun insertFavoriteAsync(vararg movie: Movie) =
        withContext(Dispatchers.IO) { async { moviesDao.insertFavorite(*movie) } }

    suspend fun deleteFavoriteAsync(vararg movie: Movie)  =
        withContext(Dispatchers.IO) { async { moviesDao.deleteFavorite(*movie) } }

    suspend fun loadFavoriteIdAsync(movieId: Long)  =
        withContext(Dispatchers.IO) { async { moviesDao.loadFavoriteId(movieId) } }

    fun loadFavorites() = moviesDao.loadFavorites()

    fun searchFavorites(query: String)  = moviesDao.searchFavorites(query)
}