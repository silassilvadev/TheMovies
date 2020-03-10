package com.silas.themovies.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.silas.themovies.data.remote.repository.MoviesRepository
import com.silas.themovies.ui.IViewProtocol
import com.silas.themovies.utils.CoRuleTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseMoviesTest {

    val repository = mockk<MoviesRepository>(relaxed = true)
    val protocol = mockk<IViewProtocol>(relaxed = true)

    @ExperimentalCoroutinesApi
    @get:Rule
    var coRuleTest = CoRuleTest()

    @get:Rule
    val rule = InstantTaskExecutorRule()
}