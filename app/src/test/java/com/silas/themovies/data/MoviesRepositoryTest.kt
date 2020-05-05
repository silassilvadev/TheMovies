package com.silas.themovies.data

import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.data.repository.movies.MoviesRepositoryImpl
import com.silas.themovies.data.source.remote.service.MoviesService
import com.silas.themovies.utils.BaseMoviesTest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MoviesRepositoryTest: BaseMoviesTest() {

    private lateinit var moviesRepository: MoviesRepository
    private val apiKey = "apiKeyMockk"
    private val language = "pt-BR"
    private val moviesService = mockk<MoviesService>(relaxed = true)

    @Before
    fun setup() {
        this.moviesRepository = MoviesRepositoryImpl(moviesService, apiKey, language)
    }

    @Test
    fun `Get all popular movies error`() {
        every {
            moviesService.loadPopulars(any(), any(), any())
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        moviesRepository
            .loadPopulars(1)
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Get all popular movies success`() {
        every {
            moviesService.loadPopulars(any(), any(), any())
        } returns Single.just(mockk())

        moviesRepository
            .loadPopulars(1)
            .test()
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `Search movies error`() {
        every {
            moviesService.searchMovies(any(), any(), any(), any())
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        moviesRepository
            .searchMovies(1, "abc")
            .test()
            .assertError(Throwable::class.java)
            .assertNotComplete()
    }

    @Test
    fun `Search movies success`() {
        every {
            moviesService.searchMovies(any(), any(), any(), any())
        } returns Single.just(mockk())

        moviesRepository
            .searchMovies(1, "abc")
            .test()
            .assertNoErrors()
            .assertComplete()
    }
}