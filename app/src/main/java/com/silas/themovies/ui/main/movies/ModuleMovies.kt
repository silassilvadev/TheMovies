package com.silas.themovies.ui.main.movies

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.repository.movies.MoviesRepository
import com.silas.themovies.data.repository.movies.MoviesRepositoryImpl
import com.silas.themovies.di.ModulesFactory.MOVIES_MODULE
import com.silas.themovies.utils.getSystemLanguage
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModuleMovies {

    val presenter = module {
        scope(named(MOVIES_MODULE)) {
            scoped { (view: MoviesContract.View) -> MoviesPresenter(view, get(), get()) }
        }
    }

    val repository = module {
        scope(named(MOVIES_MODULE)) {
            scoped<MoviesRepository> {
                MoviesRepositoryImpl(get(), BuildConfig.API_KEY, getSystemLanguage())
            }
        }
    }
}