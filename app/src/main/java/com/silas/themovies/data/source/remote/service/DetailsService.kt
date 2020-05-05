package com.silas.themovies.data.source.remote.service

import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsService {

    @GET("movie/{movie_id}")
    fun loadDetails(@Path("movie_id") movieId: Long,
                    @Query("api_key") apiKey: String,
                    @Query("language") language: String): Single<Movie>

    @GET("movie/{movie_id}/similar")
    fun loadRelated(@Path("movie_id") movieId: Long,
                    @Query("api_key") apiKey: String,
                    @Query("language") language: String,
                    @Query("page") page: Int): Single<PagedMovies>

}