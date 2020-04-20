package com.silas.themovies.ui

sealed class LoadingState {
    object Show: LoadingState()
    object Hide: LoadingState()
}