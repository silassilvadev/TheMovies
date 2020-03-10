package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.local.database.AppDatabase
import com.silas.themovies.data.local.database.MoviesDatabase
import com.silas.themovies.data.remote.client.ClientService
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.ui.IViewProtocol
import com.silas.themovies.ui.detail.DetailsViewModel
import com.silas.themovies.ui.main.movies.MoviesViewModel
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
        single<MoviesService> { ClientService.createNewService(BuildConfig.THE_MOVIES_URL) }
    }

    /** [IViewProtocol] Parameter used to return error messages when trying to perform an action */
    private val viewModelModule = module {
        viewModel { (protocol: IViewProtocol) ->
            MoviesViewModel(get(), protocol)
        }
        viewModel { (protocol: IViewProtocol) ->
            DetailsViewModel(get(),protocol)
        }
    }

    private val repositoryModule = module {
        single { MoviesRepository(get(), get<AppDatabase>().movieDao() ) }
    }

    val modules = listOf(applicationModule, viewModelModule, repositoryModule)
}
