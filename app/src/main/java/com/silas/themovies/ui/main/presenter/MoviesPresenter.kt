package com.silas.themovies.ui.main.presenter

import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.model.dto.response.PagedMovies
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MoviesPresenter(var view: MoviesContract.View?,
                      private val compositeDisposable: CompositeDisposable,
                      private val repository: MoviesRepository): MoviesContract.Presenter {

    override fun getPopulars(page: Int, query: String) =
        if (query.isBlank()) loadPopulars(page) else searchPopulars(query, page)

    override fun getFavorites(query: String) =
        if (query.isBlank()) loadFavorites() else searchFavorites(query)

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun loadPopulars(page: Int) {
        view?.apply {
            updateLoading(LoadingState.SHOW)
            val pagedListMoviesDto = PagedListMoviesDto(page)

            compositeDisposable.addAll(
                repository.loadPopulars(pagedListMoviesDto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateMovies(it)
                        updateLoading(LoadingState.HIDE)
                    }, {
                        responseError(it.message ?: it.localizedMessage ?: "")
                        updateLoading(LoadingState.HIDE)
                    })
            )
        }
    }

    private fun searchPopulars(query: String, page: Int) {
        view?.apply {
            updateLoading(LoadingState.SHOW)
            val pagedListMoviesDto = PagedListMoviesDto(page, query)

            compositeDisposable.addAll(
                repository.searchPopulars(pagedListMoviesDto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.updateMovies(it)
                        updateLoading(LoadingState.HIDE)
                    }, {
                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                        updateLoading(LoadingState.HIDE)
                    })
            )
        }
    }

    private fun loadFavorites() {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.add(
                repository.loadFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.updateMovies(PagedMovies(results = it as ArrayList<Movie>))
                        updateLoading(LoadingState.HIDE)
                    }, {
                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                        updateLoading(LoadingState.HIDE)
                    })
            )
        }
    }

    private fun searchFavorites(query: String) {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.add(
                repository.searchFavorites(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        view?.updateMovies(PagedMovies(results = it as ArrayList<Movie>))
                        updateLoading(LoadingState.HIDE)
                    }, {
                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                        updateLoading(LoadingState.HIDE)
                    })
            )
        }
    }
}