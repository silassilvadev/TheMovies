package com.silas.themovies.ui.details

import com.silas.themovies.data.repository.details.DetailsRepository
import com.silas.themovies.utils.BaseMoviesTest
import com.silas.themovies.ui.LoadingState
import com.silas.themovies.ui.detail.presenter.DetailsContract
import com.silas.themovies.ui.detail.presenter.DetailsPresenter
import io.mockk.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Before
import org.junit.Test
import java.lang.NullPointerException

class DetailsPresenterTest : BaseMoviesTest() {

    private val detailsRepository = mockk<DetailsRepository>(relaxed = true)
    private val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)
    private val detailsView = mockk<DetailsContract.View>(relaxed = true)
    private lateinit var detailsPresenter: DetailsContract.Presenter

    @Before
    fun setUp() {
        detailsPresenter = DetailsPresenter(detailsView, compositeDisposable, detailsRepository)
    }

    @Test
    fun `Load details movie error`() {
        every {
            detailsRepository.loadDetails(any())
        } returns Single.error(Throwable("Invalid Movie Id"))

        detailsPresenter.loadDetails(-1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.responseError(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Load details movie success`() {
        every {
            detailsRepository.loadDetails(any())
        } returns Single.just(mockk(relaxed = true))

        detailsPresenter.loadDetails(1)

        verify {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateMovieDetails(any())
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Load related movies not found`() {
        every {
            detailsRepository.loadRelated(any(), any())
        } returns Single.just(mockk {
            every { totalResults } returns 0
        })

        detailsPresenter.loadRelated(1)

        verify {
            detailsView.updateRelated(any())
            detailsView.responseError(any())
        }

        verify(exactly = 0) {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Load related movies error`() {
        every {
            detailsRepository.loadRelated(any(), any())
        } returns Single.error(Throwable("Invalid data returned"))

        detailsPresenter.loadRelated(1)

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0) {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Load related movies success`() {
        every {
            detailsRepository.loadRelated(any(), any())
        } returns Single.just(mockk {
            every { totalResults } returns 1
        })

        detailsPresenter.loadRelated(1)

        verify {
            detailsView.updateRelated(any())
        }

        verify(exactly = 0) {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Load related movies paginated success`() {
        every {
            detailsRepository.loadRelated(any(), any())
        } returns Single.just(mockk {
            every { totalResults } returns 1
        })

        detailsPresenter.loadRelated(1, true)

        verify {
            detailsView.updateRelated(any())
        }

        verify(exactly = 0) {
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test(expected = NullPointerException::class)
    fun `Verify is favorite invalid`(){
        every {
            detailsRepository.checkFavoriteId(any())
        } returns Maybe.just(null)

        detailsPresenter.checkIsFavorite(1)

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Verify is favorite error`() {
        every {
            detailsRepository.checkFavoriteId(any())
        } returns Maybe.error(Throwable("Invalid favorites"))

        detailsPresenter.checkIsFavorite(1)

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Verify is favorite success`() {
        every {
            detailsRepository.checkFavoriteId(any())
        } returns Maybe.just(mockk(relaxed = true))

        detailsPresenter.checkIsFavorite(1)

        verify {
            detailsView.isFavorite(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Insert favorites error`() {
        every {
            detailsRepository.insertFavorite(any())
        } returns Maybe.error(Throwable("Invalid insert favorite"))

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns true
        })

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Insert favorites success invalid`() {
        every {
            detailsRepository.insertFavorite(any())
        } returns Maybe.just(arrayListOf())

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns true
        })

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Insert favorites success`() {
        every {
            detailsRepository.insertFavorite(any())
        } returns Maybe.just(listOf(1L))

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns true
        })

        verify {
            detailsView.updateFavorite(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Remove favorites error`() {
        every {
            detailsRepository.deleteFavorite(any())
        } returns Maybe.error(Throwable("Invalid remove favorite"))

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns false
        })

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Remove favorite success invalid`() {
        every {
            detailsRepository.deleteFavorite(any())
        } returns Maybe.just(0)

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns false
        })

        verify {
            detailsView.responseError(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Remove favorite success`() {
        every {
            detailsRepository.deleteFavorite(any())
        } returns Maybe.just(1)

        detailsPresenter.updateFavorite(mockk {
            every { hasFavorite } returns false
        })

        verify {
            detailsView.updateFavorite(any())
        }

        verify(exactly = 0){
            detailsView.updateLoading(LoadingState.SHOW)
            detailsView.updateLoading(LoadingState.HIDE)
        }
    }

    @Test
    fun `Checking if view is destroyed`() {
        every {
            detailsRepository.loadDetails(any())
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
