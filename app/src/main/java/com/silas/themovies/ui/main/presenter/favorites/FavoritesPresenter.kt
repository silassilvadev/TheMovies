package com.silas.themovies.ui.main.presenter.favorites

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritesPresenter(var view: FavoritesContract.View?,
                         private val compositeDisposable: CompositeDisposable,
                         private val favoritesRepository: FavoritesRepository): FavoritesContract.Presenter {

    override fun loadFavorites(query: String) {
        if (query.isNotEmpty()) searchFavorites(query) else loadFavorites()
    }

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    private fun loadFavorites() {
        view?.apply {
            compositeDisposable.add(
                favoritesRepository.loadFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { updateLoading(LoadingState.Show) }
                    .doFinally { updateLoading(LoadingState.Hide) }
                    .subscribe({

                        setUpUpdateMovies(it as ArrayList<Movie>)
                    }, {

                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    private fun searchFavorites(query: String) {
        view?.apply {
            compositeDisposable.add(
                favoritesRepository.searchFavorites(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { updateLoading(LoadingState.Show) }
                    .doFinally { updateLoading(LoadingState.Hide) }
                    .subscribe({

                        setUpUpdateMovies(it as ArrayList<Movie>)
                    }, {

                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                    })
            )
        }
    }

    private fun setUpUpdateMovies(favorites: ArrayList<Movie>) {
        view?.updateFavorites(favorites)
        if (favorites.isEmpty()) view?.responseError("Nenhum favorito encontrado")
    }
}