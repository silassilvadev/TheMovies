package com.silas.themovies.di

import com.silas.themovies.BuildConfig
import com.silas.themovies.data.repository.details.DetailsRepository
import com.silas.themovies.data.repository.details.DetailsRepositoryImpl
import com.silas.themovies.di.ModulesFactory.DETAILS_MODULE
import com.silas.themovies.ui.detail.presenter.DetailsContract
import com.silas.themovies.ui.detail.presenter.DetailsPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ModuleDetails {

    val presenter = module {
        scope(named(DETAILS_MODULE)){
            factory { (view: DetailsContract.View) -> DetailsPresenter(view, get(), get()) }
        }
    }

    val repository = module {
        scope(named(DETAILS_MODULE)) {
            scoped<DetailsRepository> {
                DetailsRepositoryImpl(get(), get(), BuildConfig.API_KEY, BuildConfig.API_LANGUAGE)
            }
        }
    }
}