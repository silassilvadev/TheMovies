package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.source.local.database.MoviesDatabase
import com.silas.themovies.data.source.remote.client.ClientService
import com.silas.themovies.data.source.remote.service.DetailsService
import com.silas.themovies.data.source.remote.service.MoviesService
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