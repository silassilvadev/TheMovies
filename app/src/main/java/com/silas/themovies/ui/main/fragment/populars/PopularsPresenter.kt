package com.silas.themovies.ui.main.fragment.populars

import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularsPresenter(var view: PopularsContract.View?,
                        private val compositeDisposable: CompositeDisposable,
                        private val moviesRepository: MoviesRepository): PopularsContract.Presenter {

    companion object {
        private const val INITIAL_PAGE = 1
    }

    private var currentPage = INITIAL_PAGE
    private var currentPagedMovies = PagedMovies()

    override fun loadMovies(query: String, isNextPage: Boolean) {
        if (isNextPage) currentPage++
        if (query.isNotEmpty()) {
            currentPage = INITIAL_PAGE
            searchPopulars(query)
        } else loadPopulars()
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun loadPopulars() {
        updateLoading(LoadingState.SHOW)

        compositeDisposable.addAll(
            moviesRepository.loadPopulars(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    updateMovies(it)
                    updateLoading(LoadingState.HIDE)
                }, {

                    updateError(it)
                    updateLoading(LoadingState.HIDE)
                })
        )
    }

    private fun searchPopulars(query: String) {
        updateLoading(LoadingState.SHOW)

        compositeDisposable.addAll(
            moviesRepository.searchMovies(currentPage, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    updateMovies(it)
                    updateLoading(LoadingState.HIDE)
                }, {

                    updateError(it)
                    updateLoading(LoadingState.HIDE)
                })
        )
    }

    private fun updateMovies(newPagedMovies: PagedMovies) {
        this.currentPagedMovies.update(newPagedMovies)
        view?.updateMovies(this.currentPagedMovies)

        if (newPagedMovies.totalResults == 0) view?.responseError("Nenhum filme encontrado")
    }

    private fun updateLoading(state: LoadingState) {
        if (currentPage == INITIAL_PAGE) view?.updateLoading(state)
    }

    private fun updateError(error: Throwable) {
        view?.responseError(error.message ?: error.localizedMessage ?: "")
    }
}