package com.silas.themovies.ui.generic

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.R
import com.silas.themovies.ui.IProtocolError
import com.silas.themovies.utils.extensions.getContentView
import com.silas.themovies.utils.extensions.hideProgress

/**
 * Is a generic class, for generic actions, as for example error messages
 *
 * @sample GenericFragment class YourFragment: GenericFragment() { ... }
 * @author Silas at 27/02/2020
 */
open class GenericFragment: Fragment(), IProtocolError {

    override fun onResponseError(message: String) {
        hideProgress()
        activity?.let {
            val correctMessage =
                if (message.isBlank()) getString(R.string.error_service_generic_response) else message
            Snackbar.make(it.getContentView(), correctMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}