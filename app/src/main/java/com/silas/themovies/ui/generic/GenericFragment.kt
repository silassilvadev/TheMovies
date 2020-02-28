package com.silas.themovies.ui.generic

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.ui.IProtocolError
import com.silas.themovies.utils.extensions.hideProgress

open class GenericFragment: Fragment(), IProtocolError {

    override fun onResponseError(message: String) {
        hideProgress()
        activity?.findViewById<ViewGroup>(android.R.id.content)?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

}