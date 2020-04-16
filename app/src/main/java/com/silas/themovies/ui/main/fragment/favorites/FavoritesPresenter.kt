package com.silas.themovies.ui.main.fragment.favorites

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.model.entity.Movie
import com.silas.themovies.ui.LoadingState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoritesPresenter(var view: FavoritesContract.View?,
                         private val compositeDisposable: CompositeDisposable,
                         private val favoritesRepository: FavoritesRepository
): FavoritesContract.Presenter {

    override fun destroy() {
        compositeDisposable.dispose()
        view = null
    }

    override fun loadMovies(query: String) {
        if (query.isNotEmpty()) searchFavorites(query) else loadFavorites()
    }

    private fun loadFavorites() {
        view?.apply {
            updateLoading(LoadingState.SHOW)

            compositeDisposable.add(
                favoritesRepository.loadFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        setUpUpdateMovies(it as ArrayList<Movie>)
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
                favoritesRepository.searchFavorites(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({

                        setUpUpdateMovies(it as ArrayList<Movie>)
                        updateLoading(LoadingState.HIDE)
                    }, {

                        view?.responseError(it.message ?: it.localizedMessage ?: "")
                        updateLoading(LoadingState.HIDE)
                    })
            )
        }
    }

    private fun setUpUpdateMovies(favorites: ArrayList<Movie>) {
        view?.updateFavorites(favorites)
        if (favorites.isEmpty()) view?.responseError("Nenhum favorito encontrado")
    }
}