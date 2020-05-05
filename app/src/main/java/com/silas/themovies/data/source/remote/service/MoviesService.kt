package com.silas.themovies.data.source.remote.service

import com.silas.themovies.model.entity.PagedMovies
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Here the Http requests are mounted, that is, where are the service's end points,
 * as well as the objects to be sent in them and objects returned by the themes
 *
 * @author Silas at 22/02/2020
 */
interface MoviesService {

    @GET("movie/popular")
    fun loadPopulars(@Query("api_key") apiKey: String,
                     @Query("language") language: String,
                     @Query("page") page: Int): Single<PagedMovies>

    @GET("search/movie")
    fun searchMovies(@Query("api_key") apiKey: String,
                     @Query("language") language: String,
                     @Query("page") page: Int,
                     @Query("query") query: String): Single<PagedMovies>
}