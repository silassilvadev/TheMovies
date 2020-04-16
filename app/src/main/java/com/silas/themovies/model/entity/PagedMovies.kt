package com.silas.themovies.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PagedMovies(var page: Int = 1,
                  @SerializedName("total_results") var totalResults: Int = 0,
                  @SerializedName("total_pages") var totalPages: Int = 1,
                  var results: ArrayList<Movie> = arrayListOf()): Serializable {

    // Updates the page and adds the new results delivered by [newPagedListMovies]
    fun update(newPagedMovies: PagedMovies) {
        if (!equals(newPagedMovies)) {
            this.page = newPagedMovies.page
            this.totalResults = newPagedMovies.totalResults
            this.totalPages = newPagedMovies.totalPages

            if (page > 1 && totalResults > 0) this.results.addAll(newPagedMovies.results)
            else this.results = newPagedMovies.results
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other as? PagedMovies)?.let {
            it.page == page
                    && it.totalResults == totalResults
                    && it.totalPages == totalPages
                    && results.containsAll(it.results)
        } ?: false
    }

    override fun hashCode(): Int {
        var result = page
        result = 31 * result + totalResults
        result = 31 * result + totalPages
        result = 31 * result + results.hashCode()
        return result
    }
}
