package com.silas.themovies.ui.generic

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.R
import com.silas.themovies.ui.main.activity.MainActivity
import com.silas.themovies.utils.extensions.animateTransition
import com.silas.themovies.utils.extensions.getContentView
import com.silas.themovies.utils.extensions.hideProgress

/**
 * Is a generic class, for generic actions, as for example error messages and animations execute
 *
 * @sample GenericActivity class YourActivity: GenericActivity() { ... }
 * @author Silas at 27/02/2020
 */
open class GenericActivity: AppCompatActivity() {

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

    internal fun onMessage(message: String) {
        hideProgress()
        val correctMessage =
            if (message.isBlank()) getString(R.string.error_service_generic_response) else message
        Snackbar.make(getContentView(), correctMessage, Snackbar.LENGTH_SHORT).show()
    }

    /**
     * Set up a custom toolbar, with its title and home as up button option
     *
     * @param toolbar Your customized Toolbar
     * @param newTitle The title shown on the Toolbar
     * @param isHomeAsUpEnabled To inform if Toolbar should contain Home as Up button
     */
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