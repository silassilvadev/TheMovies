package com.silas.themovies.model.state

sealed class LoadingState {
    object Show: LoadingState()
    object Hide: LoadingState()
}