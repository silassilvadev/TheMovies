package com.silas.themovies.utils.extensions

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * Intent call for result facilitator in Fragments
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Fragment.startActivity(vararg params: Pair<String, Any>) {
    Intent(context, T::class.java).apply {
        setupParams(*params)
        startActivity(this)
    }
}

/**
 * Intent call for result facilitator in Fragments
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Fragment.startActivityForResult(requestCode: Int,
                                                                 vararg params: Pair<String, Any>) {
    Intent(context, T::class.java).apply {
        setupParams(*params)
        startActivityForResult(this, requestCode)
    }
}

/**
 * Starts a dialog of indeterminate progress
 */
fun Fragment.showProgress() = (activity as? AppCompatActivity)?.showProgress()

/**
 * Ends a dialog of indeterminate progress
 */
fun Fragment.hideProgress() = (activity as? AppCompatActivity)?.hideProgress()
