package com.silas.themovies.ui.detail.presenter

import com.silas.themovies.data.repository.details.DetailsRepository
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.model.entity.PagedMovies
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsPresenter(var view: DetailsContract.View?,
                       private val compositeDisposable: CompositeDisposable,
                       private val detailsRepository: DetailsRepository): DetailsContract.Presenter {

    companion object {
        private const val INITIAL_PAGE_RELATED = 1
        private const val PERSISTENCE_ERROR = "Ocorreu um erro inexperado"
    }

    private var currentPageRelated = INITIAL_PAGE_RELATED
    private var currentPagedMoviesRelated = PagedMovies()

    override fun loadDetails(movieId: Long) {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.addAll(
                detailsRepository.loadDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateLoading(LoadingState.HIDE)
                        updateMovieDetails(it)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        updateError(it)
                    })
            )
        }
    }

    override fun loadRelated(movieId: Long, isNextPage: Boolean) {
        if (isNextPage) this.currentPageRelated++

        compositeDisposable.addAll(
            detailsRepository.loadRelated(this.currentPageRelated, movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateRelated(it)
                }, {
                    updateError(it)
                })
        )
    }
    override fun checkIsFavorite(movieId: Long) {
        compositeDisposable.addAll(
            detailsRepository.checkFavoriteId(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie ->
                    movie?.let {
                        view?.isFavorite(it.hasFavorite)
                    } ?: run {
                        view?.responseError(PERSISTENCE_ERROR)
                    }
                }, {
                    updateError(it)
                })
        )
    }

    override fun updateFavorite(movie: Movie) {
        if (movie.hasFavorite) saveFavorite(movie) else removeFavorite(movie)
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun saveFavorite(movie: Movie) {
        compositeDisposable.addAll(
            detailsRepository.insertFavorite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isNullOrEmpty()) view?.updateFavorite(movie.hasFavorite)
                    else view?.responseError(PERSISTENCE_ERROR)
                }, {
                    updateError(it)
                })
        )
    }

    private fun removeFavorite(movie: Movie) {
        compositeDisposable.addAll(
            detailsRepository.deleteFavorite(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 0) view?.updateFavorite(movie.hasFavorite)
                    else view?.responseError(PERSISTENCE_ERROR)
                }, {
                    updateError(it)
                })

        )
    }

    private fun updateRelated(newPagedRelated: PagedMovies) {
        this.currentPagedMoviesRelated.update(newPagedRelated)
        view?.updateRelated(this.currentPagedMoviesRelated)

        if (newPagedRelated.totalResults == 0) view?.responseError("Nenhum filme relacionado encontrado")
    }

    private fun updateError(error: Throwable) {
        view?.responseError(error.message ?: error.localizedMessage ?: "")
    }

}