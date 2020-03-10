package com.silas.themovies.ui

/**
 * Error message return protocol to the UI
 *
 * @author Silas at 25/02/2020
 */
interface IViewProtocol {
    fun onResponseError(message: String)
}