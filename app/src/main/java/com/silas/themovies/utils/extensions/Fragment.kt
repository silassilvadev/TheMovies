package com.silas.themovies.utils.extensions

import android.app.Activity
import android.content.Intent
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

/**
 * It helps to add an updatable Layout without the need to do it via xml,
 * and gives the possibility to pass actions to be done when the update method is called.
 *
 * @param childId Resource id to be added to a SwipeRefreshLayout
 * @param functions Functions to be performed during the update
 * @return Return updated view
 */
fun Fragment.addSwipeRefreshRoot(childId: Int, vararg functions: (() -> Unit)?): View? {
    return activity?.let { itFragmentActivity ->
        val swipeRefreshLayout = SwipeRefreshLayout(itFragmentActivity).apply {
            setColorSchemeColors(context.myGetColor(R.color.colorAccent))
            setOnRefreshListener {
                functions.forEach {
                    it?.invoke()
                }.run {
                    if (isRefreshing) isRefreshing = false
                }
            }
        }


        View.inflate(context, childId, swipeRefreshLayout).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}
