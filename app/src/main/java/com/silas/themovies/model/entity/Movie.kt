package com.silas.themovies.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Favorite")
data class Movie(@PrimaryKey(autoGenerate = true)
                 @ColumnInfo(name = "id")
                 val id: Long,

                 @ColumnInfo(name = "title")
                 val title: String,

                 @ColumnInfo(name = "popularity")
                 val popularity: String,

                 @ColumnInfo(name = "overview")
                 val overview: String,

                 @SerializedName("release_date")
                 @ColumnInfo(name = "release_date")
                 val releaseDate: String,

                 @SerializedName("vote_average")
                 @ColumnInfo(name = "vote_average")
                 val voteAverage: String,

                 @Embedded
                 val genres: ArrayList<Genre>?,

                 @SerializedName("backdrop_path")
                 @ColumnInfo(name = "backdrop_path")
                 var endPointBackDrop: String = "",

                 @SerializedName("poster_path")
                 @ColumnInfo(name = "poster_path")
                 var endPointPosterPath: String = "",

                 @ColumnInfo(name = "has_favorite")
                 var hasFavorite: Boolean): Parcelable
