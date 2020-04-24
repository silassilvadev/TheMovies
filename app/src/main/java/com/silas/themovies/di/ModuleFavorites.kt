package com.silas.themovies.di

import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.data.repository.favorites.FavoritesRepositoryImpl
import com.silas.themovies.di.ModulesFactory.FAVORITES_MODULE
import com.silas.themovies.ui.main.presenter.favorites.FavoritesContract
import com.silas.themovies.ui.main.presenter.favorites.FavoritesPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModuleFavorites {

    val presenter = module {
        scope(named(FAVORITES_MODULE)) {
            factory { (view: FavoritesContract.View) ->
                FavoritesPresenter(view, get(), get())
            }
        }
    }

    val repository = module {
        scope(named(FAVORITES_MODULE)){
            scoped<FavoritesRepository> {
                FavoritesRepositoryImpl(get())
            }
        }
    }
}