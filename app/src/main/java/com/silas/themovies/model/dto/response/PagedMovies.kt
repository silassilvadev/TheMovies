package com.silas.themovies.model.dto.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PagedMovies(var page: Int = 1,
                       @SerializedName("total_results") var totalResults: Int = 0,
                       @SerializedName("total_pages") var totalPages: Int = 1,
                       var results: ArrayList<Movie>) : Serializable {

    // Updates the page and adds the new results delivered by [newPagedListMovies]
    fun updatePage(newPagedMovies: PagedMovies) {
        this.page = newPagedMovies.page
        this.totalResults = newPagedMovies.totalResults
        this.totalPages = newPagedMovies.totalPages
        this.results.addAll(newPagedMovies.results)
    }
}
