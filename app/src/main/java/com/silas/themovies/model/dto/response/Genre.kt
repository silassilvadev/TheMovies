package com.silas.themovies.model.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Entity(tableName= "Genre")
@Parcelize
data class Genre(@ColumnInfo(name = "id") val id: Long,
                 @ColumnInfo(name = "name") val name: String): Parcelable