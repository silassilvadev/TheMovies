package com.silas.themovies.ui.details

import com.silas.themovies.model.BaseMoviesTest
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.detail.presenter.DetailsContract
import com.silas.themovies.ui.detail.presenter.DetailsPresenter
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class DetailsViewModelTest : BaseMoviesTest() {

    private val detailsView: DetailsContract.View = mockk(relaxed = true)
    private lateinit var detailsPresenter: DetailsContract.Presenter

    @Before
    fun setUp() {
        detailsPresenter = DetailsPresenter(detailsView, compositeDisposable, repository)
    }

    @Test
    fun `Details not found`() {
        every {
            repository.loadDetails(any())
        } returns Single.error(Throwable("Invalid Movie Id"))

        detailsPresenter.loadDetails(-1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Related not found`() {
        every {
            repository.loadRelated(any())
        } returns Single.error(Throwable("Invalid data returned"))

        detailsPresenter.loadRelated(1, -1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Errors verify is favorite`() {
        every {
            repository.checkFavoriteId(any())
        } returns Maybe.error(Throwable("Invalid favorites"))

        detailsPresenter.checkFavoriteId(-1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Errors insert favorites`() {
        every {
            repository.insertFavorite(any())
        } returns Maybe.error(Throwable("Invalid insert favorite"))

        val movieInsert = mockk<Movie>(relaxed = true) {
            every { hasFavorite } returns true
        }

        detailsPresenter.updateFavorite(movieInsert)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Errors remove favorites`() {
        every {
            repository.deleteFavorite(any())
        } returns Maybe.error(Throwable("Invalid remove favorite"))

        val movieRemove = mockk<Movie>(relaxed = true).apply {
            every { hasFavorite } returns false
        }

        detailsPresenter.updateFavorite(movieRemove)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Success load details movie`() {
        every {
            repository.loadDetails(any())
        } returns(Single.just(mockk()))

        detailsPresenter.loadDetails(1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateMovieDetails(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Success load related movies`() {
        every {
            repository.loadRelated(any())
        } returns Single.just(mockk())

        detailsPresenter.loadRelated(1, 1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateRelated(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Success verify is favorite`() {
        every {
            repository.checkFavoriteId(any())
        } returns Maybe.just(mockk())

        detailsPresenter.checkFavoriteId(1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseFavorite(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Success insert favorite`() {
        every {
            repository.insertFavorite(any())
        } returns Maybe.just(listOf(1L))

        val movieInsert = mockk<Movie> {
            every { hasFavorite } returns true
        }

        detailsPresenter.updateFavorite(movieInsert)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateFavorite(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Success remove favorite`() {
        every {
            repository.deleteFavorite(any())
        } returns Maybe.just(1)

        val movie = mockk<Movie> {
            every { hasFavorite } returns false
        }

        detailsPresenter.updateFavorite(movie)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateFavorite(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            repository.loadDetails(any())
        } returns Single.just(mockk(relaxed = true))

        detailsPresenter.destroy()
        detailsPresenter.loadDetails(1)

        verify {
            compositeDisposable.dispose()
        }

        verify(exactly = 0) {
            detailsView.updateLoading(any())
            detailsView.responseError(any())
            detailsView.updateLoading(any())
        }
    }
}
