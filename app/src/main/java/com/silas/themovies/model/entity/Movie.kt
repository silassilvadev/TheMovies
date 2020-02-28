package com.silas.themovies.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "Favorite")
data class Movie(@PrimaryKey(autoGenerate = true)
                 @ColumnInfo(name = "id")
                 val id: Long,
                 @ColumnInfo(name = "title")
                 val title: String,
                 @ColumnInfo(name = "popularity")
                 val popularity: String,
                 @SerializedName("release_date")
                 @ColumnInfo(name = "release_date")
                 val date: String,
                 @SerializedName("vote_average")
                 @ColumnInfo(name = "vote_average")
                 val vote: String,
                 @ColumnInfo(name = "has_favorite")
                 var hasFavorite: Boolean,
                 @SerializedName("backdrop_path")
                 @ColumnInfo(name = "backdrop_path")
                 val endPointBackDrop: String,
                 @SerializedName("poster_path")
                 @ColumnInfo(name = "poster_path")
                 val endPointPosterPath: String): Serializable
