package com.silas.themovies.utils.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.silas.themovies.R
import com.silas.themovies.utils.custom.ProgressDialogCustom

inline fun <reified T: Activity> Activity.startActivity(vararg params: Pair<String, Any>) {
    Intent(this, T::class.java).apply {
        setupParams(*params)
        startActivity(this)
    }
}

inline fun <reified T: Activity> Activity.startActivityForResult(requestCode: Int,
                                                                 vararg params: Pair<String, Any>) {
    Intent(this, T::class.java).apply {
        setupParams(*params)
        startActivityForResult(this, requestCode)
    }
}

fun Activity.animateTransition(isNext: Boolean = true) {
    if (isNext) this.overridePendingTransition(R.anim.from_right_in, R.anim.from_left_out)
    else this.overridePendingTransition(R.anim.from_left_in, R.anim.from_right_out)
}

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

// region AppCompatActivity
fun AppCompatActivity.showProgress() = ProgressDialogCustom.instance.show(this.supportFragmentManager)

fun AppCompatActivity.hideProgress() = ProgressDialogCustom.instance.dismiss()
//endregion
