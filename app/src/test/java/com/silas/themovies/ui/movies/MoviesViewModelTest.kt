package com.silas.themovies.ui.movies

import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.main.fragment.favorites.FavoritesPresenter
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class MoviesViewModelTest: BaseMoviesTest() {

    private val moviesView: MoviesContract.View = mockk(relaxed = true)
    private lateinit var moviesPresenter: MoviesContract.Presenter

    @Before
    fun setUp() {
        moviesPresenter = FavoritesPresenter(moviesView, compositeDisposable, repository)
    }

    @Test
    fun `Get all popular movies returns error`() {
        //Given
        every {
            repository.loadPopulars(any())
        } returns Single.error(Throwable("Invalid data returned"))

        //When
        moviesPresenter.getPopulars()

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search popular movies returns error`() {
        //Given
        every {
            repository.searchPopulars(any())
        } returns Single.error(Throwable("Invalid data returned"))

        //When
        moviesPresenter.getPopulars(1, "Movie non-existent")

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns error`() {
        //Given
        every {
            repository.loadFavorites()
        } returns Maybe.error(Throwable("Invalid data returned"))

        //When
        moviesPresenter.getFavorites()

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search favorite movies returns error`() {
        //Given
        every {
            repository.searchFavorites(any())
        } returns  Maybe.error(Throwable("Invalid data returned"))

        //When
        moviesPresenter.getFavorites("Favorite non-existent")

        //Then
        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.responseError(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all popular movies returns successful`() {
        every {
            repository.loadPopulars(any())
        } returns Single.just(mockk())

        moviesPresenter.getPopulars()

        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search all popular movies returns successful`() {
        every {
            repository.searchPopulars(any())
        } returns Single.just(mockk())

        moviesPresenter.getPopulars(1, "Parasita")

        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns successful`() {
        every {
            repository.loadFavorites()
        } returns Maybe.just(arrayListOf())

        moviesPresenter.getFavorites()

        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search favorite movies returns successful`() {
        every {
            repository.searchFavorites(any())
        } returns Maybe.just(arrayListOf(mockk()))

        moviesPresenter.getFavorites("Parasita")

        verifyAll {
            moviesView.updateLoading(LoadingState.SHOW)
            moviesView.updateMovies(any())
            moviesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            repository.loadPopulars(any())
        } returns Single.just(mockk(relaxed = true))

        moviesPresenter.destroy()
        moviesPresenter.getPopulars(1, "")

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
