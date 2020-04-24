package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.sources.local.database.MoviesDatabase
import com.silas.themovies.data.sources.remote.client.ClientService
import com.silas.themovies.data.sources.remote.service.DetailsService
import com.silas.themovies.data.sources.remote.service.MoviesService
import io.reactivex.disposables.CompositeDisposable
import org.koin.dsl.module

object ModuleApplication {

    val application = module {
        single { MoviesDatabase.instance(get()).favoritesDao() }
        single<MoviesService> { ClientService.createNewService(BuildConfig.THE_MOVIES_URL) }
        single<DetailsService> { ClientService.createNewService(BuildConfig.THE_MOVIES_URL) }
        factory { CompositeDisposable() }
    }

}