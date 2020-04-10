package com.silas.themovies.ui.detail.presenter

import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.model.dto.request.MovieDetailsDto
import com.silas.themovies.model.dto.request.PagedListMoviesDto
import com.silas.themovies.model.dto.response.Movie
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsPresenter(var view: DetailsContract.View?,
                       private val compositeDisposable: CompositeDisposable,
                       private val repository: MoviesRepository): DetailsContract.Presenter {

    override fun loadDetails(movieId: Long) {
        val movieDetailsDto = MovieDetailsDto(movieId)

        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                repository.loadDetails(movieDetailsDto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        updateMovieDetails(it)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    override fun loadRelated(page: Int, movieId: Long) {
        val pagedListMoviesDto = PagedListMoviesDto(page, movieId = movieId)

        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                repository.loadRelated(pagedListMoviesDto)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        updateRelated(it)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    override fun updateFavorite(vararg movie: Movie) {
        movie.forEach {
            if (it.hasFavorite) saveFavorite(*movie) else removeFavorite(*movie)
        }
    }

    override fun checkFavoriteId(movieId: Long) {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                repository.checkFavoriteId(movieId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        responseFavorite(it)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun saveFavorite(vararg movie: Movie) {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                repository.insertFavorite(*movie)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        updateFavorite(!it.isNullOrEmpty())
                    }, {
                        updateLoading(LoadingState.HIDE)
                        responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    private fun removeFavorite(vararg movie: Movie) {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                repository.deleteFavorite(*movie)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        updateFavorite(it > 0)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        responseError(it.message ?: it.localizedMessage ?: "")
                    })

            )
        }
    }

}