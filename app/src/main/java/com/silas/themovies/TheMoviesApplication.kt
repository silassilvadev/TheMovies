package com.silas.themovies

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.silas.themovies.di.ModulesFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TheMoviesApplication: Application() {

    companion object {
        lateinit var application: TheMoviesApplication private set
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        application = this

        startKoin {
            androidLogger()
            androidContext(this@TheMoviesApplication)
            modules(ModulesFactory.modules)
        }
    }
}