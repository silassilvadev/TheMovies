package com.silas.themovies.model.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.silas.themovies.model.entity.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
class PagedListMovies(val page: Int,
                      @SerializedName("total_results") val totalResults: Int,
                      @SerializedName("total_pages") val totalPages: Int,
                      val results: List<Movie>) : Parcelable
