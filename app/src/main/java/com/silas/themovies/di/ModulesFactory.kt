package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.local.database.AppDatabase
import com.silas.themovies.data.local.database.MoviesDatabase
import com.silas.themovies.data.remote.client.ClientService
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.ui.detail.presenter.DetailsContract
import com.silas.themovies.ui.detail.presenter.DetailsPresenter
import com.silas.themovies.ui.main.presenter.MoviesContract
import com.silas.themovies.ui.main.presenter.MoviesPresenter
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Configuration of dependency injections in the necessary modules of the App
 *
 * @author Silas at 26/02/2020
 */
object ModulesFactory {

    private val applicationModule = module {
        single { MoviesDatabase.instance(get()) }
        factory { CompositeDisposable() }
        single<MoviesService> { ClientService.createNewService(BuildConfig.THE_MOVIES_URL) }
    }

    private val presenterModule = module {
        factory { (view: MoviesContract.View) -> MoviesPresenter(view, get(), get()) }
        factory { (view: DetailsContract.View) -> DetailsPresenter(view, get(), get()) }
    }

    private val repositoryModule = module {
        single { MoviesRepository(get(), get<AppDatabase>().movieDao() ) }
    }

    val modules = listOf(applicationModule, presenterModule, repositoryModule)
}
