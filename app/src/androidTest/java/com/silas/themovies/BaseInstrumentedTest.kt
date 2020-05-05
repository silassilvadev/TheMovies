package com.silas.themovies

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.runner.RunWith
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseInstrumentedTest(activityClass: Class<out Activity>) {

    @get:Rule
    val rule = ActivityTestRule(activityClass)
}
