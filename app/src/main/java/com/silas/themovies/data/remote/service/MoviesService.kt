package com.silas.themovies.data.remote.service

import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.model.dto.response.Movie
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Here the Http requests are mounted, that is, where are the service's end points,
 * as well as the objects to be sent in them and objects returned by the themes
 *
 * @author Silas at 22/02/2020
 */
interface MoviesService {

    @GET("movie/popular")
    suspend fun loadPopulars(@Query("api_key") apiKey: String,
                             @Query("language") language: String,
                             @Query("page") page: Int): PagedMovies

    @GET("search/movie")
    suspend fun searchPopulars(@Query("api_key") apiKey: String,
                               @Query("language") language: String,
                               @Query("page") page: Int,
                               @Query("query") query: String): PagedMovies

    @GET("movie/{movie_id}")
    suspend fun loadDetails(@Path("movie_id") idMovie: Long,
                            @Query("api_key") apiKey: String,
                            @Query("language") language: String): Movie

    @GET("movie/{movie_id}/similar")
    suspend fun searchRelated(@Path("movie_id") idMovie: Long,
                              @Query("api_key") apiKey: String,
                              @Query("language") language: String,
                              @Query("page") page: Int): PagedMovies
}