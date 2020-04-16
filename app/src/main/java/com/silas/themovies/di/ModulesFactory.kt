package com.silas.themovies.di

import com.silas.themovies.BuildConfig.*
import com.silas.themovies.data.repository.details.DetailsRepository
import com.silas.themovies.data.repository.details.DetailsRepositoryImpl
import com.silas.themovies.data.repository.favorites.FavoritesRepository
import com.silas.themovies.data.repository.favorites.FavoritesRepositoryImpl
import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.data.repository.movies.MoviesRepositoryImpl
import com.silas.themovies.data.sources.local.database.MoviesDatabase
import com.silas.themovies.data.sources.remote.client.ClientService
import com.silas.themovies.data.sources.remote.service.MoviesService
import com.silas.themovies.data.sources.remote.service.DetailsService
import com.silas.themovies.ui.detail.presenter.DetailsContract
import com.silas.themovies.ui.detail.presenter.DetailsPresenter
import com.silas.themovies.ui.main.presenter.movies.MoviesContract
import com.silas.themovies.ui.main.presenter.movies.MoviesPresenter
import com.silas.themovies.ui.main.presenter.favorites.FavoritesContract
import com.silas.themovies.ui.main.presenter.favorites.FavoritesPresenter
import io.reactivex.disposables.CompositeDisposable
import org.koin.dsl.module

/**
 * Configuration of dependency injections in the necessary modules of the App
 *
 * @author Silas at 26/02/2020
 */

object ModulesFactory {

    private val applicationModule = module {
        single { MoviesDatabase.instance(get()).favoritesDao() }
        single<MoviesService> { ClientService.createNewService(THE_MOVIES_URL) }
        single<DetailsService> { ClientService.createNewService(THE_MOVIES_URL) }
        factory { CompositeDisposable() }
    }

    private val presenterModule = module {
        factory { (view: MoviesContract.View) -> MoviesPresenter(view, get(), get()) }
        factory { (view: FavoritesContract.View) ->
            FavoritesPresenter(
                view,
                get(),
                get()
            )
        }
        factory { (view: DetailsContract.View) -> DetailsPresenter(view, get(), get()) }
    }

    private val repositoryModule = module {
        single<MoviesRepository> {
            MoviesRepositoryImpl(
                get(),
                API_KEY,
                API_LANGUAGE
            )
        }
        single<FavoritesRepository> {
            FavoritesRepositoryImpl(
                get()
            )
        }
        single<DetailsRepository> {
            DetailsRepositoryImpl(
                get(),
                get(),
                API_KEY,
                API_LANGUAGE
            )
        }
    }

    val modules = listOf(applicationModule, presenterModule, repositoryModule)
}
