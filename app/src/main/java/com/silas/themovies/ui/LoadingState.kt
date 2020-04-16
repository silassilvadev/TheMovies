package com.silas.themovies.ui

enum class LoadingState {
    SHOW,
    HIDE;

    companion object {
        fun isShowing(state: LoadingState) = state == SHOW
    }
}