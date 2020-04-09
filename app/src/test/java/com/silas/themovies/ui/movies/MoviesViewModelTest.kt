package com.silas.themovies.ui.movies

import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.main.presenter.MoviesContract
import com.silas.themovies.ui.main.presenter.MoviesPresenter
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.Single
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesViewModelTest: BaseMoviesTest() {

    private val view: MoviesContract.View = mockk(relaxed = true)
    private lateinit var presenter: MoviesContract.Presenter


    @Before
    internal fun setUp() {
        presenter = MoviesPresenter(view, compositeDisposable, repository)
    }

    @Test
    fun `Get all popular movies returns error`() {
        //Given
        every {
            repository.loadPopulars(any())
        } answers { Single.error(Throwable("Invalid data returned")) }

        //When
        presenter.getPopulars(-1)

        //Then
        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.responseError(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Searching for popular movies returns error`() {
        //Given
        every {
            repository.searchPopulars(any())
        } answers { Single.error(Throwable("Invalid data returned")) }

        //When
        presenter.getPopulars(1, "Movie non-existent")

        //Then
        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.responseError(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns error`() {
        //Given
        every {
            repository.loadFavorites()
        } answers { Maybe.error(Throwable("Invalid data returned")) }

        //When
        presenter.getFavorites("")

        //Then
        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.responseError(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Searching for favorite movies returns error`() {
        //Given
        every {
            repository.searchFavorites(any())
        } answers { Maybe.error(Throwable("Invalid data returned")) }

        //When
        presenter.getFavorites("Favorite non-existent")

        //Then
        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.responseError(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all popular movies returns successful`() {
        every {
            repository.loadPopulars(any())
        } returns Single.just(PagedMovies(1, 10000, 500, arrayListOf()))

        presenter.getPopulars(1, "")

        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.updateMovies(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Searching for all popular movies returns successful`() {
        every {
            repository.searchPopulars(any())
        } returns Single.just(PagedMovies(1, 10000, 500, arrayListOf(mockk())))


        presenter.getPopulars(1, "Parasita")

        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.updateMovies(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns successful`() {
        every {
            repository.loadFavorites()
        } returns Maybe.just(arrayListOf())

        presenter.getFavorites("")

        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.updateMovies(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Searching for favorite movies returns successful`() {
        every {
            repository.searchFavorites(any())
        } returns Maybe.just(arrayListOf(mockk()))

        presenter.getFavorites("Parasita")

        verifyAll {
            view.updateLoading(LoadingState.SHOW)
            view.updateMovies(any())
            view.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            repository.loadPopulars(any())
        } returns Single.just(mockk(relaxed = true))

        presenter.destroy()
        presenter.getPopulars()

        verify {
            compositeDisposable.dispose()
        }

        verify(exactly = 0) {
            view.updateLoading(any())
            view.updateLoading(any())
            view.responseError(any())
        }
    }

}
