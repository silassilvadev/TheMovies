package com.silas.themovies.ui.generic

import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.silas.themovies.R
import com.silas.themovies.utils.extensions.getContentView

/**
 * Is a generic class, for generic actions, as for example error messages and animations execute
 *
 * @sample GenericFragment class YourFragment: GenericFragment() { ... }
 * @author Silas at 10/03/2020
 */
open class GenericFragment: Fragment() {

    internal fun onMessage(message: String) {
        val correctMessage =
            if (message.isBlank()) getString(R.string.error_service_generic_response) else message
        activity?.apply {
            Snackbar.make(getContentView(), correctMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}