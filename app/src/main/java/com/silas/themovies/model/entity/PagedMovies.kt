package com.silas.themovies.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PagedMovies(var page: Int = 1,
                  @SerializedName("total_results") var totalResults: Int = 0,
                  @SerializedName("total_pages") var totalPages: Int = 1,
                  var results: ArrayList<Movie> = arrayListOf()): Serializable {

    // Updates the page and adds the new results delivered by [newPagedListMovies]
    fun update(newPagedMovies: PagedMovies) {
        this.page = newPagedMovies.page
        this.totalResults = newPagedMovies.totalResults
        this.totalPages = newPagedMovies.totalPages

        if (page > 1 && totalResults > 0) this.results.addAll(newPagedMovies.results)
        else this.results = newPagedMovies.results
    }
}
