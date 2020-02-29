package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.local.dao.MoviesDao
import com.silas.themovies.data.local.database.AppDatabase
import com.silas.themovies.data.local.database.MoviesDatabase
import com.silas.themovies.data.remote.client.ClientService
import com.silas.themovies.data.remote.service.MoviesService
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.ui.IProtocolError
import com.silas.themovies.ui.detail.DetailsViewModel
import com.silas.themovies.ui.generic.GenericActivity
import com.silas.themovies.ui.generic.GenericFragment
import com.silas.themovies.ui.main.movies.MoviesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Configuration of dependency injections in the necessary modules of the App
 *
 * @author Silas at 26/02/2020
 */
class Module {
    companion object {

        val appModule = module {
            single { MoviesDatabase.instance(get()) }
            single<MoviesService> { ClientService.createNewService(BuildConfig.THE_MOVIES_URL) }
        }

        val uiModule = module(override = true) {
            factory<IProtocolError> { GenericActivity() }
            factory<IProtocolError> { GenericFragment() }
        }

        val viewModelModule = module {
            viewModel { MoviesViewModel(get(), get()) }
            viewModel { DetailsViewModel(get(), get()) }
        }

        val repositoryModule = module {
            single { MoviesRepository(get(), get<AppDatabase>().movieDao() ) }
        }
    }
}