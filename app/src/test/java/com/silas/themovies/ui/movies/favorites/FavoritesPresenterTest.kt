package com.silas.themovies.ui.movies.favorites

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.utils.BaseMoviesTest
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.main.presenter.favorites.FavoritesContract
import com.silas.themovies.ui.main.presenter.favorites.FavoritesPresenter
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test

class FavoritesPresenterTest: BaseMoviesTest() {

    private val favoritesRepository = mockk<FavoritesRepository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val favoritesView = mockk<FavoritesContract.View>(relaxed = true)
    private lateinit var favoritesPresenter: FavoritesContract.Presenter

    @Before
    fun setUp() {
        this.favoritesPresenter =
            FavoritesPresenter(favoritesView, compositeDisposable, favoritesRepository)
    }

    @Test
    fun `Get all favorite movies returns invalid`() {
        //Given
        every {
            favoritesRepository.loadFavorites()
        } returns Maybe.just(arrayListOf())

        //When
        favoritesPresenter.loadFavorites()

        //Then
        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.updateFavorites(any())
            favoritesView.responseError(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns error`() {
        //Given
        every {
            favoritesRepository.loadFavorites()
        } returns Maybe.error(Throwable("Invalid data returned"))

        //When
        favoritesPresenter.loadFavorites()

        //Then
        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.responseError(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search favorite movies returns invalid`() {
        //Given
        every {
            favoritesRepository.searchFavorites(any())
        } returns Maybe.just(arrayListOf())

        //When
        favoritesPresenter.loadFavorites("Favorite non-existent")

        //Then
        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.updateFavorites(any())
            favoritesView.responseError(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search favorite movies returns error`() {
        //Given
        every {
            favoritesRepository.searchFavorites(any())
        } returns  Maybe.error(Throwable("Invalid data returned"))

        //When
        favoritesPresenter.loadFavorites("Favorite non-existent")

        //Then
        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.responseError(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Get all favorite movies returns successful`() {
        every {
            favoritesRepository.loadFavorites()
        } returns Maybe.just(arrayListOf(mockk()))

        favoritesPresenter.loadFavorites()

        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.updateFavorites(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Search favorite movies returns successful`() {
        every {
            favoritesRepository.searchFavorites(any())
        } returns Maybe.just(arrayListOf(mockk()))

        favoritesPresenter.loadFavorites("Parasita")

        verifyAll {
            favoritesView.updateLoading(LoadingState.SHOW)
            favoritesView.updateFavorites(any())
            favoritesView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            favoritesRepository.loadFavorites()
        } returns Maybe.just(mockk(relaxed = true))

        favoritesPresenter.destroy()
        favoritesPresenter.loadFavorites()

        verify {
            compositeDisposable.dispose()
        }

        verify(exactly = 0) {
            favoritesView.updateLoading(any())
            favoritesView.responseError(any())
            favoritesView.updateLoading(any())
        }
    }

}
