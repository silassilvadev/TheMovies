package com.silas.themovies.di

import com.silas.themovies.ui.detail.ModuleDetails
import com.silas.themovies.ui.main.favorite.ModuleFavorites
import com.silas.themovies.ui.main.movies.ModuleMovies

/**
 * Configuration of dependency injections in the necessary modules of the App
 *
 * @author Silas at 26/02/2020
 */

object ModulesFactory {

    const val MOVIES_MODULE = "MoviesModule"
    const val FAVORITES_MODULE = "FavoritesModule"
    const val DETAILS_MODULE = "DetailsModule"

    val modules = listOf(
        ModuleApplication.application,
        ModuleMovies.presenter,
        ModuleMovies.repository,
        ModuleFavorites.presenter,
        ModuleFavorites.repository,
        ModuleDetails.presenter,
        ModuleDetails.repository)

}
