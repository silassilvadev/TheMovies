package com.silas.themovies.model.dto.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PagedListMovies(var page: Int = 1,
                           @SerializedName("total_results") var totalResults: Int = 0,
                           @SerializedName("total_pages") var totalPages: Int = 1,
                           var results: ArrayList<Movie>) : Serializable {

    // Updates the page and adds the new results delivered by [newPagedListMovies]
    fun updatePage(newPagedListMovies: PagedListMovies) {
        this.page = newPagedListMovies.page
        this.totalResults = newPagedListMovies.totalResults
        this.totalPages = newPagedListMovies.totalPages
        this.results.addAll(newPagedListMovies.results)
    }
}
