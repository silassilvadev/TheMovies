package com.silas.themovies.ui.main

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.silas.themovies.BaseInstrumentedTest
import com.silas.themovies.R
import com.silas.themovies.utils.CustomMatchers.Companion.withIsEmpty
import com.silas.themovies.utils.CustomMatchers.Companion.withNotIsEmpty
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest: BaseInstrumentedTest(MainActivity::class.java) {

    @Before
    fun setup(){
        rule.launchActivity(Intent())
    }

    @Test
    fun checkIfViewsAreBeingInflated() {
        onView(withId(R.id.item_toolbar_search)).check(matches(isDisplayed()))
        onView(withText("POPULARES")).check(matches(isDisplayed()))
        onView(withText("FAVORITOS")).check(matches(isDisplayed()))
        onView(withId(R.id.recycler_view_movies)).check(matches(isDisplayed()))
    }

    @Test
    fun checkSeListEmptyReturnsMessage() {
        onView(withId(R.id.recycler_view_movies)).check(matches(withIsEmpty()))
        onView(withText("Nenhum filme encontrado")).check(matches(isDisplayed()))
    }

    @Test
    fun checkIfListIsFilled() {
        onView(withId(R.id.recycler_view_movies))
            .check(matches(withNotIsEmpty()))
            .check(matches(isCompletelyDisplayed()))
    }
}
