package com.silas.themovies.utils.extensions

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silas.themovies.R

/**
 * Intent call for result facilitator in Fragments
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Fragment.startActivity(vararg params: Pair<String, Any>) =
    this.activity?.startActivity<T>(*params)

/**
 * Intent call for result facilitator in Fragments
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Fragment.startActivityForResult(requestCode: Int,
                                                                 vararg params: Pair<String, Any>) {
    this.activity?.startActivityForResult<T>(requestCode, *params)
}

/**
 * Starts a dialog of indeterminate progress
 */
fun Fragment.showProgress() = (activity as? AppCompatActivity)?.showProgress()

/**
 * Ends a dialog of indeterminate progress
 */
fun Fragment.hideProgress() = (activity as? AppCompatActivity)?.hideProgress()
