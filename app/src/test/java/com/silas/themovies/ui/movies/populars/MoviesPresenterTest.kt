package com.silas.themovies.ui.movies.populars

import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.utils.BaseMoviesTest
import com.silas.themovies.model.state.LoadingState
import com.silas.themovies.ui.main.movies.MoviesContract
import com.silas.themovies.ui.main.movies.MoviesPresenter
import io.mockk.*
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test

class MoviesPresenterTest: BaseMoviesTest() {

    private val moviesRepository = mockk<MoviesRepository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val moviesView = mockk<MoviesContract.View>(relaxed = true)
    private lateinit var moviesPresenter: MoviesContract.Presenter

    @Before
    fun setUp() {
        this.moviesPresenter = MoviesPresenter(moviesView, compositeDisposable, moviesRepository)
    }

    @Test
    fun `Get all popular movies returns invalid`() {
        //Given
        every {
            moviesRepository.loadPopulars(any())
        } returns Single.just(mockk(relaxed = true))

        //When
        moviesPresenter.loadMovies()

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.updateMovies(any())
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Get all popular movies returns error`() {
        //Given
        every {
            moviesRepository.loadPopulars(any())
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        //When
        moviesPresenter.loadMovies()

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Search movies returns invalid`() {
        //Given
        every {
            moviesRepository.searchMovies(any(), any())
        } returns Single.just(mockk(relaxed = true))

        //When
        moviesPresenter.loadMovies("Movie non-existent")

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.updateMovies(any())
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Search movies returns error`() {
        //Given
        every {
            moviesRepository.searchMovies(any(), any())
        } returns Single.error(Throwable("Ocorreu um erro inexperado"))

        //When
        moviesPresenter.loadMovies("Movie non-existent")

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Get all popular movies returns successful`() {
        every {
            moviesRepository.loadPopulars(any())
        } returns Single.just(mockk {
            every { totalResults } returns 1
        })

        moviesPresenter.loadMovies()

        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Get all popular movies paginated returns successful`() {
        every {
            moviesRepository.loadPopulars(any())
        } returns Single.just(mockk {
            every { totalResults } returns 1
        })

        moviesPresenter.loadMovies(isNextPage = true)

        verifyAll {
            moviesView.updateMovies(any())
        }
    }

    @Test
    fun `Search all movies returns successful`() {
        every {
            moviesRepository.searchMovies(any(), any())
        } returns Single.just(mockk {
            every { totalResults } returns 1
        })

        moviesPresenter.loadMovies("Parasita")

        verifyAll {
            moviesView.updateLoading(LoadingState.Show)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.Hide)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            moviesRepository.loadPopulars(any())
        } returns Single.just(mockk(relaxed = true))

        moviesPresenter.destroy()
        moviesPresenter.loadMovies()

        verify {
            compositeDisposable.dispose()
        }

        verify(exactly = 0) {
            moviesView.updateLoading(any())
            moviesView.responseError(any())
            moviesView.updateLoading(any())
        }
    }

}
