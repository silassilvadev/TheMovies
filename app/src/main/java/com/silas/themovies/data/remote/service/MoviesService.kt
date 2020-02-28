package com.silas.themovies.data.remote.service

import androidx.lifecycle.LiveData
import com.silas.themovies.model.dto.response.MovieDetails
import com.silas.themovies.model.dto.response.PagedListMovies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular")
    suspend fun loadPopulars(@Query("api_key") apiKey: String,
                             @Query("language") language: String,
                             @Query("page") page: Int): PagedListMovies

    @GET("search/movie")
    suspend fun searchMovie(@Query("api_key") apiKey: String,
                            @Query("language") language: String,
                            @Query("page") page: Int,
                            @Query("query") query: String): PagedListMovies

    @GET("movie/{movie_id}/similar")
    suspend fun searchRelated(@Path("movie_id") idMovie: Long,
                              @Query("api_key") apiKey: String,
                              @Query("language") language: String,
                              @Query("page") page: Int): PagedListMovies

    @GET("movie/{movie_id}")
    suspend fun loadDetails(@Path("movie_id") idMovie: Long,
                            @Query("api_key") apiKey: String,
                            @Query("language") language: String): MovieDetails
}