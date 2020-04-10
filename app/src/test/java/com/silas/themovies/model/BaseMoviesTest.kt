package com.silas.themovies.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.utils.RxSchedulerRule
import io.mockk.mockk
import io.reactivex.disposables.CompositeDisposable
import org.junit.ClassRule
import org.junit.Rule

abstract class BaseMoviesTest {

    val repository = mockk<MoviesRepository>(relaxed = true)
    val compositeDisposable = mockk<CompositeDisposable>(relaxed = true)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val rxSchedulerRule = RxSchedulerRule()
    }
}