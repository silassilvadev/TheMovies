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

    private lateinit var movie: Movie
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
                        this@DetailsPresenter.movie = it
                        updateLoading(LoadingState.HIDE)
                        updateMovieDetails(it)
                    }, {
                        updateLoading(LoadingState.HIDE)
                        updateError(it)
                    })
            )
        }
    }

    override fun loadRelated(isNextPage: Boolean) {
        if (isNextPage) currentPageRelated++

        compositeDisposable.addAll(
            detailsRepository.loadRelated(this.currentPageRelated, this.movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateRelated(it)
                }, {
                    updateError(it)
                })
        )
    }
    override fun checkIsFavorite() {
        compositeDisposable.addAll(
            detailsRepository.checkFavoriteId(this.movie.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movie ->

                    movie?.let {
                        this.movie = it
                        view?.isFavorite(this.movie.hasFavorite)
                    } ?: error(PERSISTENCE_ERROR)
                }, {
                    updateError(it)
                })
        )
    }

    override fun updateFavorite() {
        this.movie.hasFavorite = !this.movie.hasFavorite
        if (this.movie.hasFavorite) saveFavorite() else removeFavorite()
    }


    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun saveFavorite() {
        compositeDisposable.addAll(
            detailsRepository.insertFavorite(this.movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (!it.isNullOrEmpty()) view?.updateFavorite(this.movie.hasFavorite)
                    else error(PERSISTENCE_ERROR)
                }, {
                    updateError(it)
                })
        )
    }

    private fun removeFavorite() {
        compositeDisposable.addAll(
            detailsRepository.deleteFavorite(this.movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it > 0) view?.updateFavorite(this.movie.hasFavorite)
                    else error(PERSISTENCE_ERROR)
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