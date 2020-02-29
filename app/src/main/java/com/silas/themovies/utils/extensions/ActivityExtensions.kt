package com.silas.themovies.utils.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silas.themovies.R
import com.silas.themovies.utils.custom.ProgressDialogCustom

/**
 * Intent call facilitator in Activities
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Activity.startActivity(vararg params: Pair<String, Any>) {
    Intent(this, T::class.java).apply {
        setupParams(*params)
        startActivity(this)
    }
}

/**
 * Intent call for result facilitator in Activities
 * @param params Extra intention parameters
 */
inline fun <reified T: Activity> Activity.startActivityForResult(requestCode: Int,
                                                                 vararg params: Pair<String, Any>) {
    Intent(this, T::class.java).apply {
        setupParams(*params)
        startActivityForResult(this, requestCode)
    }
}

/**
 * Performs a transition animation between Activities
 * @param isNext Sets the direction of the animation
 */
fun Activity.animateTransition(isNext: Boolean = true) {
    if (isNext) this.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    else this.overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
}

/**
 * It helps to add an updatable Layout without the need to do it via xml,
 * and gives the possibility to pass actions to be done when the update method is called.
 *
 * @param childId Resource id to be added to a SwipeRefreshLayout
 * @param functions Functions to be performed during the update
 * @return Return updated view
 */
fun Activity.addSwipeRefreshRoot(childId: Int, vararg functions: (() -> Unit)?): View {
    val swipeRefreshLayout = SwipeRefreshLayout(this).apply {
        setColorSchemeColors(myGetColor(R.color.colorAccent))
        setOnRefreshListener {
            functions.forEach {
                it?.invoke()
            }.run {
                if (isRefreshing) isRefreshing = false
            }
        }
    }

    return View.inflate(this, childId, swipeRefreshLayout).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }
}

/**
 * @return Content View
 */
fun Activity.getContentView(): ViewGroup = findViewById(R.id.content)

// region AppCompatActivity
/**
 * Starts a dialog of indeterminate progress
 */
fun AppCompatActivity.showProgress() = ProgressDialogCustom.instance.show(this.supportFragmentManager)

/**
 * Ends a dialog of indeterminate progress
 */
fun AppCompatActivity.hideProgress() = ProgressDialogCustom.instance.dismiss()
//endregion
