package com.silas.themovies.utils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.silas.themovies.utils.RxSchedulerRule
import io.mockk.mockk
import io.reactivex.disposables.CompositeDisposable
import org.junit.ClassRule
import org.junit.Rule

abstract class BaseMoviesTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val rxSchedulerRule = RxSchedulerRule()
    }
}