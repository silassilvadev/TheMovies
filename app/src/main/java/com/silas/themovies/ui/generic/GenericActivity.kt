package com.silas.themovies.ui.generic

import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.ui.IProtocolError
import com.silas.themovies.ui.main.MainActivity
import com.silas.themovies.utils.extensions.animateTransition
import com.silas.themovies.utils.extensions.hideProgress

open class GenericActivity: AppCompatActivity(), IProtocolError {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        animateTransition(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && this !is MainActivity) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.animateTransition(false)
    }

    override fun finish() {
        super.finish()
        this.animateTransition(false)
    }

    override fun onResponseError(message: String) {
        hideProgress()
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        Snackbar.make(contentView, message, Snackbar.LENGTH_SHORT).show()
    }

    internal fun setUpCustomToolbar(toolbar: Toolbar,
                                    newTitle: String,
                                    isHomeAsUpEnabled: Boolean = true){
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(isHomeAsUpEnabled)
            title = newTitle
        }
    }
}